package com.example.rickmorty.domain.episodes

import com.example.rickmorty.domain.characters.CharactersUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import javax.inject.Inject

class AboutEpisodeInteract @Inject constructor(
    private val aboutCharacterUseCase: CharactersUseCase,
    private val aboutEpisodeUseCase: EpisodesUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getDetailAboutEpisode(id: Int): Flow<AboutEpisodeModel> =
        aboutEpisodeUseCase.getAboutEpisode(id)
            .flatMapConcat { episodeModel ->
                val characters = episodeModel.charactersList.asFlow().flatMapConcat { characterId ->
                    aboutCharacterUseCase.getAboutCharacter(characterId)
                }.toList()
                flowOf(Pair(episodeModel, characters))
            }
            .map { pair ->
                val episode = pair.first
                val characters = pair.second
                AboutEpisodeModel(
                    id = episode.id,
                    nameEpisode = episode.name,
                    airDate = episode.airDate,
                    dateEpisode = episode.dateEpisode,
                    charactersList = characters
                )
            }
}