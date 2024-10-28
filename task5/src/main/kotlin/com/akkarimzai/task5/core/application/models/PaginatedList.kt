package com.akkarimzai.task5.core.application.models

import kotlin.math.ceil

class PaginatedList<T>(items: Collection<T>, count: Int, page: Int, size: Int) {
    val page: Int = page
    val totalPages: Int = ceil(count / size.toDouble()).toInt()
    val items: MutableList<T> = mutableListOf()

    init {
        this.items.addAll(items)
    }
}