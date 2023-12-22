package com.example.rickmorty.presentation.locations.aboutLocation.recycler.model

data class AboutLocationDetailModel(
    val name: String,
    val type: String,
    val dimension: String,
) : AboutLocationRecyclerModel()