package com.example.rickmorty.data

import com.example.rickmorty.data.characters.api.AboutCharacterApi
import com.example.rickmorty.data.characters.api.CharactersApiResponse
import com.example.rickmorty.data.locations.api.AboutLocationApi
import com.example.rickmorty.data.locations.api.LocationsApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharacterList(@Query("page") page: Int): Response<CharactersApiResponse>

    @GET("api/location")
    suspend fun getLocationsList(@Query("page") page: Int): Response<LocationsApiResponse>

    @GET("api/character/{id}")
    suspend fun getAboutCharacter(@Path(value = "id") id: Int): Response<AboutCharacterApi>

    @GET("api/location/{id}")
    suspend fun getAboutLocation(@Path(value = "id") id: Int): Response<AboutLocationApi>


}