package com.akkarimzai.task5.core.application.services

import com.akkarimzai.task5.core.application.contracts.persistence.repositories.ICategoryRepository
import com.akkarimzai.task5.core.application.exceptions.NotFoundException
import com.akkarimzai.task5.core.application.exceptions.ValidationException
import com.akkarimzai.task5.core.application.models.PageableList
import com.akkarimzai.task5.core.application.models.category.CreateCategory
import com.akkarimzai.task5.core.application.models.category.UpdateCategory
import com.akkarimzai.task5.core.application.profiles.toEntity
import com.akkarimzai.task5.core.domain.entities.Category
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.assertThrows
import java.util.*

class CategoryServiceTest : FunSpec() {
   init {
     context("category service tests") {
         test("save executes normally") {
             // Arrange
             val createCategory = CreateCategory("test", "test")
             val entity = createCategory.toEntity()
             val savedEntity = Category(UUID.randomUUID(), entity.name, entity.slug)
             every { repository.save(entity) } returns savedEntity

             // Act
             service.save(createCategory)

             // Assert
             verify(exactly = 1) { repository.save(entity) }
        }

         test("save throws validation exception") {
             // Arrange
             val createCategory = CreateCategory("test", "")

             // Act && Assert
             assertThrows<ValidationException> {
                 service.save(createCategory)
             }
         }

        test("load executes normally") {
            // Arrange
            val id = UUID.randomUUID()
            val category = Category(id, "test", "test")
            every { repository.load(id) } returns category

            // Act
            val loadedCategory = service.load(id)

            // Assert
            verify(exactly = 1) { repository.load(id) }
            category shouldBeEqual loadedCategory
        }

         test("load category doesn't exist should throw NotFound exception") {
             // Arrange
             val id = UUID.randomUUID()
             every { repository.load(id) } returns null

             // Act && Assert
             assertThrows<NotFoundException> {
                 service.load(id)
             }
         }

        test("list executes normally") {
            // Arrange
            val pageableList = PageableList(1, 10)
            every { repository.list(pageableList.page, pageableList.size) } returns listOf()
            every { repository.count() } returns 0

            // Act
            val paginatedList = service.list(pageableList)

            // Assert
            paginatedList.items shouldBe emptyList<Category>()
        }

         test("list page size less than zero throws validation exception") {
             // Arrange
             val pageableList = PageableList(0, 10)

             // Act && Assert
             assertThrows<ValidationException> {
                 service.list(pageableList)
             }
         }

        test("update executes normally") {
            // Arrange
            val id = UUID.randomUUID()
            val updateLocation = UpdateCategory("test", "test")
            val entity = Category(id, "test", "test")
            every { repository.load(id) } returns entity
            every { repository.update(entity) } just Runs

            // Act
            service.update(id, updateLocation)

            // Assert
            verify(exactly = 1) { repository.update(entity) }
        }

         test("update with invalid request should throw ValidationException") {
             // Arrange
             val id = UUID.randomUUID()
             val updateLocation = UpdateCategory("", "test")

             // Act && Assert
             assertThrows<ValidationException> {
                 service.update(id, updateLocation)
             }
         }

        test("delete executes normally") {
            // Arrange
            val id = UUID.randomUUID()
            val entity = Category(id, "test", "test")
            every { repository.load(id) } returns entity
            every { repository.delete(id) } just Runs

            // Act
            service.delete(id)

            // Assert
            verify(exactly = 1) { repository.load(id) }
            verify(exactly = 1) { repository.delete(id) }
        }
     }
   }
   var repository: ICategoryRepository = mockk()
   val service: CategoryService = CategoryService(repository)
}
