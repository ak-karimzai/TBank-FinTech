package com.akkarimzai.task10.entities

import jakarta.persistence.*

@Entity
@Table(name = "places")
data class Place(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val name: String,
    val address: String,
    val description: String,
    val subway: String? = null,
    @OneToMany(mappedBy = "place", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val events: MutableList<Event> = mutableListOf()
) {
    override fun toString(): String {
        return "Place(id=$id, name='$name', address='$address', description='$description', subway=$subway)"
    }
}