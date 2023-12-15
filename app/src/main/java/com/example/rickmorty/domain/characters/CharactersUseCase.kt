package com.example.rickmorty.domain.characters

import com.example.rickmorty.data.characters.CharactersListRepository
import javax.inject.Inject

class CharactersUseCase @Inject constructor(
    private val charactersListRepository: CharactersListRepository
) {

    suspend fun getCharactersList() = charactersListRepository.getCharactersList()
}