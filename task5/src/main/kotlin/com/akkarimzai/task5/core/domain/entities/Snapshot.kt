package com.akkarimzai.task5.core.domain.entities

import com.akkarimzai.task5.core.domain.common.Entity
import java.time.LocalDateTime
import java.util.*

class Snapshot<T : Entity>(
    id: UUID? = null,
    val state: T,
    val changedAt: LocalDateTime = LocalDateTime.now(),
) : Entity(id)