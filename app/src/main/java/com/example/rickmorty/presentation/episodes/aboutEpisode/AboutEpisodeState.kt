package com.example.rickmorty.presentation.episodes.aboutEpisode

import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.AboutEpisodeCharactersModel

data class AboutEpisodeState(
    val nameEpisode: String,
    val airDate: String,
    val dateEpisode: String,
    val episodeModelList: List<AboutEpisodeCharactersModel>
)