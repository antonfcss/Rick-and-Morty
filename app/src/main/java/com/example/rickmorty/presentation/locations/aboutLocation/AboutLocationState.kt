package com.example.rickmorty.presentation.locations.aboutLocation

import com.example.rickmorty.presentation.locations.aboutLocation.recycler.AboutLocationCharactersModel

data class AboutLocationState(
    val name: String,
    val type: String,
    val dimension: String,
    val locationModelList: List<AboutLocationCharactersModel>
)