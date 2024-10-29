package com.akkarimzai.task5.core.application.models.category

import com.akkarimzai.task5.core.application.exceptions.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class UpdateCategoryTest : FunSpec({
    test("Should update the category") {
        // Arrange
        val category = UpdateCategory("test", "test")

        // Act
        val validationResult = category.validate()

        // Assert
        validationResult.size shouldBe  0
    }

    test("Empty update throws BadRequestException") {
        // Arrange
        val category = UpdateCategory(null, null)

        // Act && Assert
        shouldThrow<BadRequestException> {
            category.validate()
        }
    }

    test("Unvalid slug causes validation fault") {
        // Arrange
        val category = UpdateCategory("", null)

        // Act
        val validationResult = category.validate()

        // Assert
        validationResult.size shouldNotBe   0
    }

    test("Unvalid name causes validation fault") {
        // Arrange
        val category = UpdateCategory("test", "")

        // Act
        val validationResult = category.validate()

        // Assert
        validationResult.size shouldNotBe   0
    }
})
