package com.akkarimzai.task5.core.domain.entities

import com.akkarimzai.task5.core.domain.common.Entity
import java.util.UUID

class Category(
    id: UUID,
    var slug: String,
    var name: String
) : Entity(id) {
    constructor(
        slug: String, name: String
    ) : this(UUID(0L, 0L), slug, name)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Category

        if (id != other.id) return false
        if (slug != other.slug) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + slug.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

    override fun toString(): String {
        return "Category(id=$id, slug='$slug', name='$name')"
    }
}