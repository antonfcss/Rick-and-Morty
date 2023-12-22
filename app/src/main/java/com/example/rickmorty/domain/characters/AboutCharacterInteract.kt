package com.example.rickmorty.domain.characters

import com.example.rickmorty.domain.episodes.EpisodesUseCase
import com.example.rickmorty.domain.locations.LocationUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class AboutCharacterInteract @Inject constructor(
    private val aboutCharacterUseCase: CharactersUseCase,
    private val aboutLocationUseCase: LocationUseCase,
    private val aboutEpisodeUseCase: EpisodesUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getDetailAboutCharacter(id: Int): Flow<AboutCharacterModel> =
        aboutCharacterUseCase.getAboutCharacter(id)
            .flatMapConcat { characterModel ->
                aboutLocationUseCase.getAboutLocation(characterModel.location?.id ?: 0)
                    .zip(flowOf(characterModel)) { locations, character -> character to locations }
                    .flatMapConcat { pair ->
                        val episodes = pair.first.episode.asFlow().flatMapConcat { episodeId ->
                            aboutEpisodeUseCase.getAboutEpisode(episodeId)
                        }.toList()
                        flowOf(Triple(pair.first, pair.second, episodes))
                    }
            }
            .map { triple ->
                val character = triple.first
                val location = triple.second
                val episodes = triple.third
                AboutCharacterModel(
                    id = character.id,
                    name = character.name,
                    status = character.status,
                    species = character.species,
                    type = character.type,
                    gender = character.gender,
                    originName = character.origin?.name,
                    originId = character.origin?.id,
                    locationName = location.name,
                    locationId = location.id,
                    image = character.image,
                    episodeList = episodes
                )
            }

}
