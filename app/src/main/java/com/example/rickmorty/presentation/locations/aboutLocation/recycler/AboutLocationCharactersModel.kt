package com.example.rickmorty.presentation.locations.aboutLocation.recycler

import android.graphics.Bitmap

data class AboutLocationCharactersModel(
    val characterId: Int,
    val characterName: String,
    val characterSpecies: String,
    val characterStatus: String,
    val characterGender: String,
    val image: Bitmap
)