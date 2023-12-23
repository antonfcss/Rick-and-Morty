package com.example.rickmorty.data.episodes

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.rickmorty.base.extractLastPartToInt
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.local.entities.EpisodesCharacterEntity
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity
import com.example.rickmorty.data.episodes.paging.EpisodesPagingSource
import com.example.rickmorty.data.episodes.paging.LocalEpisodePagingSource
import com.example.rickmorty.domain.episodes.EpisodesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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
        val pagingSourceFactory: () -> PagingSource<Int, EpisodesModel> = {
            if (internetManager.isInternetConnected()) {
                episodesPagingSource.getPagingEpisodes(
                    name = name,
                    episode = episode
                )
            } else {
                LocalEpisodePagingSource(episodesDao, name, episode)
            }
        }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow.cachedIn(scope)
    }

    suspend fun getAboutEpisodesFromApi(id: Int): Flow<EpisodesModel> {
        return if (internetManager.isInternetConnected()) {
            flow {
                try {
                    val apiAboutEpisode = api.getAboutEpisode(id)
                    if (episodesDao.searchById(id) == null) {
                        episodesDao.insertAll(
                            listOf(
                                EpisodesEntity(
                                    id = apiAboutEpisode.id,
                                    name = apiAboutEpisode.name,
                                    airDate = apiAboutEpisode.airDate,
                                    episode = apiAboutEpisode.episode,
                                    characters = EpisodesCharacterEntity(apiAboutEpisode.characters.map { it.extractLastPartToIntOrZero() })
                                )
                            )
                        )
                    }
                    emit(
                        EpisodesModel(
                            id = apiAboutEpisode.id,
                            name = apiAboutEpisode.name,
                            airDate = apiAboutEpisode.airDate,
                            dateEpisode = apiAboutEpisode.episode,
                            charactersList = apiAboutEpisode.characters.map { it.extractLastPartToInt() }
                        )
                    )
                } catch (e: Exception) {
                    throw Exception("Failed to fetch episode from API", e)
                }
            }
        } else {
            val episodeEntity = episodesDao.searchById(id)
            return if (episodeEntity != null) {
                flow {
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
            } else {
                flow {
                    Log.d("EpisodesRepository", "Эпизод не найден в базе данных для id: $id")
                    throw Exception("Эпизод не найден в базе данных Episode")
                }
            }
        }
    }
}

