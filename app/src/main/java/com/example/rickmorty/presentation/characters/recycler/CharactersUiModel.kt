package com.example.rickmorty.presentation.characters.recycler

import android.graphics.Bitmap

data class CharactersUiModel(
    val id: Int,
    val characterName: String,
    val characterSpecies: String,
    val characterStatus: String,
    val characterGender: String,
    val image: Bitmap
)
