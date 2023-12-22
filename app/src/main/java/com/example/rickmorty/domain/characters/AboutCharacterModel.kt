package com.example.rickmorty.domain.characters

import android.graphics.Bitmap
import com.example.rickmorty.domain.episodes.EpisodesModel

data class AboutCharacterModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val originName: String?,
    val originId: Int?,
    val locationName: String,
    val locationId: Int,
    val image: Bitmap,
    val episodeList: List<EpisodesModel>,
)