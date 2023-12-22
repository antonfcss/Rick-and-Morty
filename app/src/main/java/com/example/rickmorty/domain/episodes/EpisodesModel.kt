package com.example.rickmorty.domain.episodes

data class EpisodesModel(
    val id: Int,
    val name: String,
    val airDate: String,
    val dateEpisode: String,
    val charactersList: List<Int>,
)