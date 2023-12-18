package com.example.rickmorty.data.characters.local

import androidx.room.TypeConverter
import com.example.rickmorty.data.characters.local.entities.CharacterLocationEntity
import com.example.rickmorty.data.characters.local.entities.EpisodesEntity
import com.example.rickmorty.data.characters.local.entities.OriginEntity
import com.google.gson.Gson

class CharactersConverters {
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
    fun fromLocationModel(locationEntity: String): CharacterLocationEntity {
        val gson = Gson()
        val entity = gson.fromJson(locationEntity, CharacterLocationEntity::class.java)
        return entity
    }

    @TypeConverter
    fun toLocationModel(characterLocationEntity: CharacterLocationEntity): String {
        val gson = Gson()
        val json = gson.toJson(characterLocationEntity)
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