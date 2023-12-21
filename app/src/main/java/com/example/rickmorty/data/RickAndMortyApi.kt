package com.example.rickmorty.data

import com.example.rickmorty.data.characters.api.AboutCharacterApiModel
import com.example.rickmorty.data.characters.api.CharactersApiResponse
import com.example.rickmorty.data.episodes.api.AboutEpisodesApiModel
import com.example.rickmorty.data.episodes.api.EpisodesApiResponse
import com.example.rickmorty.data.locations.api.AboutLocationApiModel
import com.example.rickmorty.data.locations.api.LocationsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharacterList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
    ): Response<CharactersApiResponse>

    @GET("api/location")
    suspend fun getLocationsList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("type") type: String?,
        @Query("dimension") dimension: String?,
    ): Response<LocationsApiResponse>

    @GET("api/episode")
    suspend fun getEpisodesList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("episode") episode: String?
    ): Response<EpisodesApiResponse>

    @GET("api/episode/{id}")
    suspend fun getAboutEpisode(@Path(value = "id") id: Int): AboutEpisodesApiModel

    @GET("api/character/{id}")
    suspend fun getAboutCharacter(@Path(value = "id") id: Int): AboutCharacterApiModel

    @GET("api/location/{id}")
    suspend fun getAboutLocation(@Path(value = "id") id: Int): AboutLocationApiModel


}