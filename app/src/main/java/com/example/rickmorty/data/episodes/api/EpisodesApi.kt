package com.example.rickmorty.data.episodes.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodesApi {
    @GET("api/episode")
    suspend fun getEpisodesList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?
    ): EpisodesApiResponse

    @GET("api/episode/{id}")
    suspend fun getAboutEpisode(@Path(value = "id") id: Int): AboutEpisodesApiModel
}