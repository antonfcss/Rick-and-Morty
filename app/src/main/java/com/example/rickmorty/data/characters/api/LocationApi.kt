package com.example.rickmorty.data.characters.api

import com.google.gson.annotations.SerializedName

data class LocationApi(
    @SerializedName("name")
    val locationName: String,
    @SerializedName("url")
    val url: String
)