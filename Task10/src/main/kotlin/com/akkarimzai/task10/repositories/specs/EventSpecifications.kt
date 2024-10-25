package com.akkarimzai.task10.repositories.specs

import com.akkarimzai.task10.entities.Event
import com.akkarimzai.task10.entities.Place
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object EventSpecifications {
    fun buildSpecification(placeId: Long, name: String?, fromDate: LocalDateTime?, toDate: LocalDateTime?): Specification<Event> {
        return Specification<Event> { root, query, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            predicates.add(criteriaBuilder.equal(root.get<Place>("place").get<Long>("id"), placeId))

            name?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%${name.lowercase()}%"))
            }
            fromDate?.let {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), it))
            }
            toDate?.let {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), it))
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}