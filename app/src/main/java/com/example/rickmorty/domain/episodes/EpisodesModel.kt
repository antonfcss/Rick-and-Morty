package com.example.rickmorty.domain.episodes

data class EpisodesModel(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val charactersList: List<String>,
)