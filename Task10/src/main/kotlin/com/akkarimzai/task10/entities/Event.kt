package com.akkarimzai.task10.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val date: LocalDateTime,
    val tagline: String? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    val place: Place
)