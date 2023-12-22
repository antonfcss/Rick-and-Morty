package com.example.rickmorty.domain.locations

data class LocationModel(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<Int>
)