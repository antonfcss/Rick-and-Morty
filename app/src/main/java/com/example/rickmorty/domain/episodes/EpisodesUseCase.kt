package com.example.rickmorty.domain.episodes

import com.example.rickmorty.data.episodes.EpisodesRepository
import javax.inject.Inject

class EpisodesUseCase @Inject constructor(private val episodesRepository: EpisodesRepository) {
    suspend fun getPagingEpisodes() = episodesRepository.getPagingEpisodes()
}