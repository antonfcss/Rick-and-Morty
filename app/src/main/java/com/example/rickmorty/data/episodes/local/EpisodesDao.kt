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
}