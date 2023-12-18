package com.example.rickmorty.data.locations.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("locations")
data class LocationEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: ResidentsEntity,
)