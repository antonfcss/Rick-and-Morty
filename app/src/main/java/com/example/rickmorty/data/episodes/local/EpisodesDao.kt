package com.example.rickmorty.data.episodes.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity

@Dao
interface EpisodesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locationEntity: List<EpisodesEntity>)

    @Query("SELECT * FROM episodes ORDER BY id ASC")
    fun getPagingList(): List<EpisodesEntity>

    @Query("SELECT * FROM episodes WHERE name LIKE :name ORDER BY id ASC")
    fun searchByName(name: String): List<EpisodesEntity>

    @Query("SELECT * FROM episodes WHERE name LIKE :episode ORDER BY id ASC")
    fun searchByEpisode(episode: String): List<EpisodesEntity>
}