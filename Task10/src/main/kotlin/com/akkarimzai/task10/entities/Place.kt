package com.akkarimzai.task10.entities

import jakarta.persistence.*

@Entity
@Table(name = "places")
class Place(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "places_seq")
    @SequenceGenerator(name = "places_seq", sequenceName = "places_seq", allocationSize = 1)
    var id: Long? = null,
    var name: String,
    var address: String,
    var description: String,
    var subway: String? = null,
    @OneToMany(mappedBy = "place", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var events: MutableList<Event> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Place

        if (id != other.id) return false
        if (name != other.name) return false
        if (address != other.address) return false
        if (description != other.description) return false
        if (subway != other.subway) return false
        if (events != other.events) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + name.hashCode()
        result = 31 * result + address.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + (subway?.hashCode() ?: 0)
        result = 31 * result + events.hashCode()
        return result
    }
}