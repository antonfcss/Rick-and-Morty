package com.example.rickmorty.data.characters.local

import androidx.room.TypeConverter
import com.example.rickmorty.data.characters.local.entities.EpisodesEntity
import com.example.rickmorty.data.characters.local.entities.LocationEntity
import com.example.rickmorty.data.characters.local.entities.OriginEntity
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun fromOriginModel(originModel: String): OriginEntity {
        val gson = Gson()
        val entity = gson.fromJson(originModel, OriginEntity::class.java)
        return entity
    }

    @TypeConverter
    fun toOriginModel(originEntity: OriginEntity): String {
        val gson = Gson()
        val json = gson.toJson(originEntity)
        return json
    }

    @TypeConverter
    fun fromLocationModel(locationEntity: String): LocationEntity {
        val gson = Gson()
        val entity = gson.fromJson(locationEntity, LocationEntity::class.java)
        return entity
    }

    @TypeConverter
    fun toLocationModel(locationEntity: LocationEntity): String {
        val gson = Gson()
        val json = gson.toJson(locationEntity)
        return json
    }

    @TypeConverter
    fun fromEpisodesEntity(episodes: String): EpisodesEntity {
        val gson = Gson()
        val entity = gson.fromJson(episodes, EpisodesEntity::class.java)
        return entity
    }

    @TypeConverter
    fun toEpisodesEntity(episodesEntity: EpisodesEntity): String {
        val gson = Gson()
        val json = gson.toJson(episodesEntity)
        return json
    }


}