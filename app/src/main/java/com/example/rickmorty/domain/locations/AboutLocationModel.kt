package com.example.rickmorty.domain.locations

import com.example.rickmorty.domain.characters.CharactersModel

data class AboutLocationModel(
    val id: Int,
    val name: String,
    val type: String,
    val dimension: String,
    val charactersList: List<CharactersModel>
)