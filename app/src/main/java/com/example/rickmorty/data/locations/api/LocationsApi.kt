package com.example.rickmorty.data.locations.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationsApi {
    @GET("api/location")
    suspend fun getLocationsList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?,
    ): LocationsApiResponse

    @GET("api/location/{id}")
    suspend fun getAboutLocation(@Path(value = "id") id: Int): AboutLocationApiModel
}