package com.example.rickmorty.domain.characters

import android.graphics.Bitmap

data class CharactersModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: OriginModel?,
    val location: LocationModel?,
    val image: Bitmap,
    val episode: List<Int>,
)
