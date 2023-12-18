package com.example.rickmorty.data.episodes.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("episodes")
data class EpisodesEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: EpisodesCharacterEntity,
)