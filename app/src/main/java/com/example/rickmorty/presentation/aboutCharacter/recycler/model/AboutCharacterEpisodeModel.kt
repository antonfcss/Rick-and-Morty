package com.example.rickmorty.presentation.aboutCharacter.recycler.model

data class AboutCharacterEpisodeModel(
    val episodeId: Int,
    val nameEpisode: String,
    val numberEpisode: String,
    val airDataEpisode: String
) : AboutCharacterRecyclerModel()