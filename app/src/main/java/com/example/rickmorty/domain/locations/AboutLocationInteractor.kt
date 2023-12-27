package com.example.rickmorty.domain.locations

import com.example.rickmorty.domain.characters.CharactersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class AboutLocationInteractor @Inject constructor(
    private val aboutCharacterUseCase: CharactersUseCase,
    private val aboutLocationUseCase: LocationUseCase,
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getDetailAboutLocation(id: Int): Flow<AboutLocationModel> =
        aboutLocationUseCase.getAboutLocation(id)
            .flatMapConcat { locationModel ->
                val characters = locationModel.residents.asFlow().flatMapConcat { characterId ->
                    aboutCharacterUseCase.getAboutCharacter(characterId)
                }.toList()
                flowOf(Pair(locationModel, characters))
            }.map { pair ->
                val location = pair.first
                val characters = pair.second
                AboutLocationModel(
                    id = location.id,
                    name = location.name,
                    type = location.type,
                    dimension = location.dimension,
                    charactersList = characters
                )
            }
}