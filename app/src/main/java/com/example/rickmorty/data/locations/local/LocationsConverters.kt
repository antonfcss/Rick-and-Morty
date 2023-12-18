package com.example.rickmorty.data.locations.local

import androidx.room.TypeConverter
import com.example.rickmorty.data.locations.local.entities.ResidentsEntity
import com.google.gson.Gson

class LocationsConverters {
    @TypeConverter
    fun fromResidentsEntity(episodes: String): ResidentsEntity {
        val gson = Gson()
        val entity = gson.fromJson(episodes, ResidentsEntity::class.java)
        return entity
    }

    @TypeConverter
    fun toResidentsEntity(residentsEntity: ResidentsEntity): String {
        val gson = Gson()
        val json = gson.toJson(residentsEntity)
        return json
    }
}