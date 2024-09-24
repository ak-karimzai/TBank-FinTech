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
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{categoryId}")
    fun get(@PathVariable categoryId: UUID) : ResponseEntity<Category> {
        return ResponseEntity<Category>(service.load(id = categoryId), HttpStatus.OK)
    }

    @GetMapping
    fun list(@ModelAttribute pageableList: PageableList) : ResponseEntity<PaginatedList<Category>> {
        return ResponseEntity<PaginatedList<Category>>(service.list(pageableList), HttpStatus.OK)
    }

    @PostMapping
    fun create(@RequestBody category: CreateCategory) : ResponseEntity<UUID> {
        return ResponseEntity<UUID>(service.save(category), HttpStatus.CREATED)
    }

    @PutMapping("/{categoryId}")
    fun update(@PathVariable categoryId: UUID, @RequestBody category: UpdateCategory) : ResponseEntity<Unit> {
        return ResponseEntity(service.update(categoryId, category), HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/{categoryId}")
    fun delete(@PathVariable categoryId: UUID) : ResponseEntity<Unit> {
        return ResponseEntity(service.delete(categoryId), HttpStatus.NO_CONTENT)
    }
}