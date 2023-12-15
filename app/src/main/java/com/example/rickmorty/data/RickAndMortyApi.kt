package com.example.rickmorty.data

import com.example.rickmorty.data.characters.api.entities.CharactersApiResponse
import retrofit2.http.GET

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharacterList(): CharactersApiResponse
}