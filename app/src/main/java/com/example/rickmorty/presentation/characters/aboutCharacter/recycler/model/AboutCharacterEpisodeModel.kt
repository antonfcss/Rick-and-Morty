package com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model

data class AboutCharacterEpisodeModel(
    val nameEpisode: String,
    val numberEpisode: String,
    val airDataEpisode: String
) : AboutCharacterRecyclerModel()