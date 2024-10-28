package com.akkarimzai.task5.persistence.repositories.observers

import com.akkarimzai.task5.core.application.contracts.persistence.repositories.ILocationRepository
import com.akkarimzai.task5.core.domain.entities.Location
import org.springframework.stereotype.Service

@Service
class LocationObserver(
    repository: ILocationRepository
) : EntityObserver<Location>(repository)