package com.akkarimzai.task5.core.application.contracts.persistence.repositories

import com.akkarimzai.task5.core.domain.entities.Location

interface ILocationRepository : IEntityRepository<Location> {
    fun slugExist(slug: String) : Boolean
}