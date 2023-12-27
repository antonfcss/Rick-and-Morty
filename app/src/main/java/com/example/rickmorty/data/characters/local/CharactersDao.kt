package com.example.rickmorty.data.characters.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.characters.local.entities.CharacterEntity

@Dao
interface CharactersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(characterEntity: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(characterEntity: CharacterEntity)

    @Query("SELECT * FROM character ORDER BY id ASC")
    fun getPagingList(): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE name LIKE :name ORDER BY id ASC")
    fun searchByName(name: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE status LIKE :status ORDER BY id ASC")
    fun searchByStatus(status: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE species LIKE :species ORDER BY id ASC")
    fun searchBySpecies(species: String): PagingSource<Int, CharacterEntity>

    @Query("SELECT * FROM character WHERE id = :characterId")
    fun searchById(characterId: Int): CharacterEntity?

}
