package com.akkarimzai.task5.api.controllers.v1

import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.PaginatedList
import com.akkarimzai.task5.core.application.models.category.CreateCategory
import com.akkarimzai.task5.core.application.models.category.UpdateCategory
import com.akkarimzai.task5.core.application.services.CategoryService
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.persistence.annotations.logging.LogExecutionTime
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/v1/categories"])
@LogExecutionTime
class CategoryController(private val service: CategoryService) {
    @GetMapping("/{categoryId}")
    fun get(@PathVariable categoryId: UUID) : Category {
        return service.load(id = categoryId);
    }

    @GetMapping
    fun list(@ModelAttribute pageableList: PageableList) : PaginatedList<Category> {
        return service.list(pageableList)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody category: CreateCategory) : UUID {
        return service.save(category)
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable categoryId: UUID, @RequestBody category: UpdateCategory) {
        return service.update(categoryId, category)
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable categoryId: UUID) {
        return service.delete(categoryId)
    }
}