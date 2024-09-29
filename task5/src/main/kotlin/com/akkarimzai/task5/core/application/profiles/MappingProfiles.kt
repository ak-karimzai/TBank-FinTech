package com.akkarimzai.task5.core.application.profiles

import com.akkarimzai.task5.core.application.models.category.CreateCategory
import com.akkarimzai.task5.core.application.models.location.CreateLocation
import com.akkarimzai.task5.core.domain.entities.Category
import com.akkarimzai.task5.core.domain.entities.Location

fun CreateCategory.toEntity(): Category {
    return Category(
        name = this.name,
        slug = this.slug
    )
}

fun CreateLocation.toEntity(): Location {
    return Location(
        name = this.name,
        slug = this.slug
    )
}