package com.akkarimzai.task10.models.place

data class PlaceDto(
    val id: Long,
    val name: String,
    val address: String,
    val description: String,
    val subway: String?,
)