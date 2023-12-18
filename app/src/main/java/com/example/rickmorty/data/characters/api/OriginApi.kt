package com.example.rickmorty.data.characters.api

import com.google.gson.annotations.SerializedName

data class OriginApi(
    @SerializedName("name")
    val originName: String,
    @SerializedName("url")
    val url: String
)