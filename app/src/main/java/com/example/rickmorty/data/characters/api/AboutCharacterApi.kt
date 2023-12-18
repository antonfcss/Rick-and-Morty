package com.example.rickmorty.data.characters.api

import com.google.gson.annotations.SerializedName

data class AboutCharacterApi(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("species")
    val species: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("origin")
    val originApi: OriginApi,
    @SerializedName("location")
    val locationApi: LocationApi,
    @SerializedName("image")
    val image: String,
    @SerializedName("episode")
    val episode: List<String>
)