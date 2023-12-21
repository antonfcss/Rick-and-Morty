package com.example.rickmorty.data.episodes

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.paging.EpisodesPagingSource
import com.example.rickmorty.domain.episodes.EpisodesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val episodesPagingSource: EpisodesPagingSource,
    private val internetManager: InternetManager,
    private val episodesDao: EpisodesDao,
    private val api: RickAndMortyApi

) {

    fun getPagingEpisodes(name: String?, episode: String?): Flow<PagingData<EpisodesModel>> {
        if (internetManager.isInternetConnected()) {
            return Pager(config = PagingConfig(pageSize = 2, enablePlaceholders = false),
                pagingSourceFactory = {
                    episodesPagingSource.getPagingEpisodes(
                        name = name,
                        episode = episode
                    )
                }).flow.cachedIn(
                scope
            )
        } else {
            val filteredEpisodesList = {
                when {
                    name != null -> episodesDao.searchByName(name)
                    episode != null -> episodesDao.searchByEpisode(episode)
                    else -> episodesDao.getPagingList()
                }
            }.asFlow()
            return filteredEpisodesList.flatMapConcat { episodesEntities ->
                flow {
                    emit(PagingData.from(episodesEntities.map { episodesEntity ->
                        EpisodesModel(
                            id = episodesEntity.id,
                            name = episodesEntity.name,
                            airDate = episodesEntity.airDate,
                            episode = episodesEntity.episode,
                            charactersList = episodesEntity.characters.charactersList
                        )
                    }))
                }

            }
        }
    }

    suspend fun getAboutEpisodesFromApi(id: Int): Flow<EpisodesModel> {
        return flow {
            val apiAboutEpisode = api.getAboutEpisode(id)
            emit(
                EpisodesModel(
                    id = apiAboutEpisode.id,
                    name = apiAboutEpisode.name,
                    airDate = apiAboutEpisode.airDate,
                    episode = apiAboutEpisode.episode,
                    charactersList = apiAboutEpisode.characters
                )
            )
        }
    }
}

