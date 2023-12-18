package com.example.rickmorty.data.locations.api

import com.google.gson.annotations.SerializedName

data class LocationsApiResponse(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val locationsApiModels: List<LocationsApiModel>
)