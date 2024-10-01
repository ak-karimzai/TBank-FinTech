package com.akkarimzai.task5.infrastructure.services

import com.akkarimzai.task5.infrastructure.models.ApiCategory
import com.akkarimzai.task5.infrastructure.models.ApiLocation

interface INewsService {
    suspend fun fetchCategories(endpoint: String): List<ApiCategory>
    suspend fun fetchLocations(endpoint: String): List<ApiLocation>
}