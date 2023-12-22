package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model

data class AboutEpisodeDetailModel(
    val nameEpisode: String,
    val airDate: String,
    val dateEpisode: String,
) : AboutEpisodeRecyclerModel()