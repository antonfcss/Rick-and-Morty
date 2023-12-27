package com.example.rickmorty.presentation.aboutEpisode

import com.example.rickmorty.presentation.aboutEpisode.recycler.AboutEpisodeCharactersModel

data class AboutEpisodeState(
    val nameEpisode: String,
    val airDate: String,
    val dateEpisode: String,
    val episodeModelList: List<AboutEpisodeCharactersModel>
)