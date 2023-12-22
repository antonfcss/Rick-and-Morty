package com.example.rickmorty.domain.episodes

import com.example.rickmorty.domain.characters.CharactersModel

data class AboutEpisodeModel(
    val id: Int,
    val nameEpisode: String,
    val airDate: String,
    val dateEpisode: String,
    val charactersList: List<CharactersModel>,
)