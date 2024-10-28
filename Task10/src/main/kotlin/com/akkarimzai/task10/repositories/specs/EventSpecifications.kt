package com.akkarimzai.task10.repositories.specs

import com.akkarimzai.task10.entities.Event
import com.akkarimzai.task10.entities.Event_
import com.akkarimzai.task10.entities.Place_
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDateTime

object EventSpecifications {
    fun buildSpecification(placeId: Long, name: String?, fromDate: LocalDateTime?, toDate: LocalDateTime?): Specification<Event> {
        return Specification<Event> { root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()

            predicates.add(criteriaBuilder.equal(root.get(Event_.place).get(Place_.id), placeId))

            name?.let {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Event_.name)), "%${name.lowercase()}%"))
            }
            fromDate?.let {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Event_.date), it))
            }
            toDate?.let {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Event_.date), it))
            }

            criteriaBuilder.and(*predicates.toTypedArray())
        }
    }
}