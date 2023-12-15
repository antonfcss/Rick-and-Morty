package com.example.rickmorty.data.characters.api.entities

import com.google.gson.annotations.SerializedName

data class CharactersApiResponse(
    @SerializedName("info")
    val info: Info?,
    @SerializedName("results")
    val charactersResultsList: List<CharactersApiModel>
)