package com.example.rickmorty.presentation.locations.recycler

data class LocationUiModel(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)
