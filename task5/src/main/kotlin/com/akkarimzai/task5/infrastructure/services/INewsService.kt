package com.akkarimzai.task5.infrastructure.services

import com.akkarimzai.task5.infrastructure.models.ApiCategory
import com.akkarimzai.task5.infrastructure.models.ApiLocation

interface INewsService {
    fun fetchCategories(endpoint: String): List<ApiCategory>
    fun fetchLocations(endpoint: String): List<ApiLocation>
}