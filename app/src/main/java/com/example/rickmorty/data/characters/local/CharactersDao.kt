package com.example.rickmorty.data.characters.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.characters.local.entities.CharacterEntity

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characterEntity: List<CharacterEntity>)

    @Query("SELECT * FROM character ORDER BY id ASC")
    fun getPagingList(): List<CharacterEntity>

    @Query("SELECT * FROM character WHERE name LIKE :name ORDER BY id ASC")
    fun searchByName(name: String): List<CharacterEntity>

    @Query("SELECT * FROM character WHERE status LIKE :status ORDER BY id ASC")
    fun searchByStatus(status: String): List<CharacterEntity>

    @Query("SELECT * FROM character WHERE species LIKE :species ORDER BY id ASC")
    fun searchBySpecies(species: String): List<CharacterEntity>

}
