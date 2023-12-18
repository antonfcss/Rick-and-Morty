package com.example.rickmorty.data.locations.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.locations.local.entities.LocationEntity

@Dao
interface LocationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locationEntity: List<LocationEntity>)

    @Query("SELECT * FROM locations ORDER BY name ASC LIMIT :limit OFFSET :offset")
    fun getPagingList(limit: Int, offset: Int): List<LocationEntity>
}