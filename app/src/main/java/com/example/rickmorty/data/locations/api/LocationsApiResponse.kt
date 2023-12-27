package com.example.rickmorty.data.locations.api

import com.google.gson.annotations.SerializedName

data class LocationsApiResponse(
    @SerializedName("results")
    val locationsApiModels: List<LocationsApiModel>
)