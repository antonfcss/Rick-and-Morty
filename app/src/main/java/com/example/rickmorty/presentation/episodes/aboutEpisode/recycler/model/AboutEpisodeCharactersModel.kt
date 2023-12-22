package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model

import android.graphics.Bitmap

data class AboutEpisodeCharactersModel(
    val characterName: String,
    val characterSpecies: String,
    val characterStatus: String,
    val characterGender: String,
    val image: Bitmap
) : AboutEpisodeRecyclerModel()