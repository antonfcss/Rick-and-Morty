package com.example.rickmorty.data.episodes.api

import com.google.gson.annotations.SerializedName

data class EpisodesApiResponse(
    @SerializedName("results")
    val episodesApiModels: List<EpisodesApiModel>
)