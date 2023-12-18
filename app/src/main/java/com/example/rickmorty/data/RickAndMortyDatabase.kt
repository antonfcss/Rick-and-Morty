package com.example.rickmorty.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickmorty.data.characters.local.CharactersConverters
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.local.entities.CharacterEntity
import com.example.rickmorty.data.episodes.local.EpisodesConverters
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity
import com.example.rickmorty.data.locations.local.LocationsConverters
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.local.entities.LocationEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class, EpisodesEntity::class],
    version = 1
)
@TypeConverters(CharactersConverters::class, LocationsConverters::class, EpisodesConverters::class)
abstract class RickAndMortyDatabase : RoomDatabase() {

    abstract fun characterDao(): CharactersDao

    abstract fun locationDao(): LocationsDao

    abstract fun episodesDao(): EpisodesDao
}