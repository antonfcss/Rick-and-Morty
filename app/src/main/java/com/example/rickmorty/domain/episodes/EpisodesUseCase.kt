package com.example.rickmorty.domain.episodes

import com.example.rickmorty.data.episodes.EpisodesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodesRepository: EpisodesRepository) {
    fun getPagingEpisodes(name: String?, episode: String?) =
        episodesRepository.getPagingEpisodes(name = name, episode = episode)

    suspend fun getAboutEpisode(id: Int): Flow<EpisodesModel> {
        return episodesRepository.getAboutEpisodesFromApi(id)
    }
}