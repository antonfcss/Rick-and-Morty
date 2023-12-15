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

    @Query("SELECT * FROM character")
    fun getAll(): List<CharacterEntity>


}
