package com.akkarimzai.task5.core.application.models.category

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class CreateCategoryTest : FunSpec({
    test("Invalid slug in category creation should throw exception") {
        // Arrange
        val result: CreateCategory = CreateCategory("", "test")

        // Act
        val validationResult = result.validate()

        // Assert
        validationResult.size shouldNotBe  0
    }

    test("Invalid name in category creation should throw exception") {
        // Arrange
        val result: CreateCategory = CreateCategory("test", "")

        // Act
        val validationResult = result.validate()

        // Assert
        validationResult.size shouldNotBe  0
    }

    test("Invalid category creation should throw exception") {
        // Arrange
        val result: CreateCategory = CreateCategory("", "")

        // Act
        val validationResult = result.validate()

        // Assert
        validationResult.size shouldNotBe  0
    }

    test("Valid category creation shouldn't throw exception") {
        // Arrange
        val result: CreateCategory = CreateCategory("test", "test")

        // Act
        val validationResult = result.validate()

        // Assert
        validationResult.size shouldBe 0
    }
})
