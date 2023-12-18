package com.example.rickmorty.data.episodes.local

import androidx.room.TypeConverter
import com.example.rickmorty.data.episodes.local.entities.EpisodesCharacterEntity
import com.google.gson.Gson

class EpisodesConverters {
    @TypeConverter
    fun fromResidentsEntity(episodes: String): EpisodesCharacterEntity {
        val gson = Gson()
        val entity = gson.fromJson(episodes, EpisodesCharacterEntity::class.java)
        return entity
    }

    @TypeConverter
    fun toResidentsEntity(episodesCharacterEntity: EpisodesCharacterEntity): String {
        val gson = Gson()
        val json = gson.toJson(episodesCharacterEntity)
        return json
    }
}