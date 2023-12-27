package com.example.rickmorty.data.episodes

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickmorty.base.extractLastPartToInt
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.episodes.api.EpisodesApi
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.local.entities.EpisodesCharacterEntity
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity
import com.example.rickmorty.data.episodes.paging.EpisodesPagingSource
import com.example.rickmorty.data.episodes.paging.LocalEpisodePagingSource
import com.example.rickmorty.domain.episodes.EpisodesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val internetManager: InternetManager,
    private val episodesDao: EpisodesDao,
    private val episodesApi: EpisodesApi
) {

    fun getPagingEpisodes(name: String?, episode: String?): Flow<PagingData<EpisodesModel>> =
        if (internetManager.isInternetConnected()) {
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = {
                    EpisodesPagingSource(
                        episodesDao,
                        episodesApi,
                        name,
                        episode
                    )
                }
            ).flow.cachedIn(scope)
        } else {
            Pager(
                config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = { LocalEpisodePagingSource(episodesDao, name, episode) }
            ).flow.map {
                it.map { episodeEntity ->
                    EpisodesModel(
                        id = episodeEntity.id,
                        name = episodeEntity.name,
                        airDate = episodeEntity.airDate,
                        dateEpisode = episodeEntity.episode,
                        charactersList = episodeEntity.characters.charactersList
                    )
                }
            }.cachedIn(scope)
        }

    suspend fun getAboutEpisodesFromApi(id: Int): Flow<EpisodesModel> = flow {
        if (internetManager.isInternetConnected()) {
            val apiAboutEpisode = episodesApi.getAboutEpisode(id)
            episodesDao.insert(
                EpisodesEntity(
                    id = apiAboutEpisode.id,
                    name = apiAboutEpisode.name,
                    airDate = apiAboutEpisode.airDate,
                    episode = apiAboutEpisode.episode,
                    characters = EpisodesCharacterEntity(apiAboutEpisode.characters.map { it.extractLastPartToIntOrZero() })
                )
            )
            emit(
                EpisodesModel(
                    id = apiAboutEpisode.id,
                    name = apiAboutEpisode.name,
                    airDate = apiAboutEpisode.airDate,
                    dateEpisode = apiAboutEpisode.episode,
                    charactersList = apiAboutEpisode.characters.map { it.extractLastPartToInt() }
                )
            )
        } else {
            val episodeEntity = episodesDao.searchById(id)
            episodeEntity?.let {
                emit(
                    EpisodesModel(
                        id = episodeEntity.id,
                        name = episodeEntity.name,
                        airDate = episodeEntity.airDate,
                        dateEpisode = episodeEntity.episode,
                        charactersList = episodeEntity.characters.charactersList
                    )
                )
            }

        }
    }
}

