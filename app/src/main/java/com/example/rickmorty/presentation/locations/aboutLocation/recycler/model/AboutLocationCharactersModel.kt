package com.example.rickmorty.presentation.locations.aboutLocation.recycler.model

import android.graphics.Bitmap

data class AboutLocationCharactersModel(
    val characterName: String,
    val characterSpecies: String,
    val characterStatus: String,
    val characterGender: String,
    val image: Bitmap
) : AboutLocationRecyclerModel()