package com.example.rickmorty.data.characters.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApi {
    @GET("api/character")
    suspend fun getCharacterList(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?,
        @Query("species") species: String?,
    ): CharactersApiResponse

    @GET("api/character/{id}")
    suspend fun getAboutCharacter(@Path(value = "id") id: Int): AboutCharacterApiModel
}