package com.example.rickmorty.data.characters.api.entities

import com.google.gson.annotations.SerializedName

data class Origin(
    @SerializedName("name")
    val originName: String,
    @SerializedName("url")
    val url: String
)