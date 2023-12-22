package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler

import android.graphics.Bitmap

data class AboutEpisodeCharactersModel(
    val characterId: Int,
    val characterName: String,
    val characterSpecies: String,
    val characterStatus: String,
    val characterGender: String,
    val image: Bitmap
)