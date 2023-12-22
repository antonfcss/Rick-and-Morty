package com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model

import android.graphics.Bitmap

data class AboutCharacterUiModel(
    val characterName: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String?,
    val locationId: Int,
    val locationName: String,
    val image: Bitmap,
) : AboutCharacterRecyclerModel()
