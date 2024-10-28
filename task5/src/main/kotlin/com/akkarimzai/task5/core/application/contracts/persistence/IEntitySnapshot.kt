package com.akkarimzai.task5.core.application.contracts.persistence

import com.akkarimzai.task5.core.domain.common.Entity
import com.akkarimzai.task5.core.domain.entities.Snapshot
import java.util.UUID

interface ISnapshotable<T : Entity> {
    fun createSnapshot(state: T)
    fun getSnapshots(id: UUID): List<Snapshot<T>>?
    fun getLastState(id: UUID): Snapshot<T>?
}