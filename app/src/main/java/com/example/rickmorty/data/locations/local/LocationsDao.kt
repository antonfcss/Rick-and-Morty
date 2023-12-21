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

    @Query("SELECT * FROM locations ORDER BY id ASC")
    fun getPagingList(): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE name LIKE :name ORDER BY id ASC")
    fun searchByName(name: String): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE type LIKE :type ORDER BY id ASC")
    fun searchByType(type: String): List<LocationEntity>

    @Query("SELECT * FROM locations WHERE dimension LIKE :dimension ORDER BY id ASC")
    fun searchByDimension(dimension: String): List<LocationEntity>
}