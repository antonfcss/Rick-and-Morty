package com.example.rickmorty.domain.characters

import com.example.rickmorty.data.characters.CharactersListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharactersUseCase @Inject constructor(
    private val charactersListRepository: CharactersListRepository
) {

    suspend fun getPagingCharacters(
        name: String?,
        status: String?,
        species: String?,
    ) = charactersListRepository.getPagingCharacters(
        name = name,
        status = status,
        species = species,
    )

    suspend fun getAboutCharacter(id: Int): Flow<CharactersModel> {
        return charactersListRepository.getAboutCharacterFromApi(id)
    }
}
