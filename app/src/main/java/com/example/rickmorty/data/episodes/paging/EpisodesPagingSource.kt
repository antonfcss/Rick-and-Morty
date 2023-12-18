package com.example.rickmorty.data.episodes.paging

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.base.BaseSource
import com.example.rickmorty.base.Results
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.local.entities.EpisodesCharacterEntity
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity
import com.example.rickmorty.domain.episodes.EpisodesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class EpisodesPagingSource @Inject constructor(
    private val baseSource: BaseSource,
    private val context: Context,
    private val internetManager: InternetManager,
    private val episodesDao: EpisodesDao,
    private val api: RickAndMortyApi
) : BaseSource by baseSource {

    fun getPagingEpisodes(): PagingSource<Int, EpisodesModel> {
        return object : PagingSource<Int, EpisodesModel>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesModel> {
                val position = params.key ?: 0
                if (internetManager.isInternetConnected()) {
                    return try {
                        when (val response = oneShotCalls { api.getEpisodesList(position * 2) }) {
                            is Results.Success -> {
                                val episodesModelList =
                                    response.data.episodesApiModels.map { episodesApiModel ->
                                        EpisodesModel(
                                            id = episodesApiModel.id,
                                            name = episodesApiModel.name,
                                            airDate = episodesApiModel.airDate,
                                            episode = episodesApiModel.episode,
                                            charactersList = episodesApiModel.characters
                                        )
                                    }
                                withContext(Dispatchers.IO) {
                                    episodesDao.insertAll(episodesModelList.map { episodesModel ->
                                        EpisodesEntity(
                                            id = episodesModel.id,
                                            name = episodesModel.name,
                                            airDate = episodesModel.airDate,
                                            episode = episodesModel.episode,
                                            characters = EpisodesCharacterEntity(episodesModel.charactersList)
                                        )
                                    })
                                }
                                if (episodesModelList.isEmpty()) {
                                    LoadResult.Error(Throwable("Empty data"))
                                } else {
                                    LoadResult.Page(
                                        data = episodesModelList,
                                        prevKey = if (position == 0) null else position - 1,
                                        nextKey = if (response.data.episodesApiModels.isEmpty()) null else position + 1
                                    )
                                }
                            }

                            is Results.Error -> {
                                LoadResult.Error(response.exception)
                            }
                        }
                    } catch (exception: IOException) {
                        LoadResult.Error(exception)
                    } catch (exception: HttpException) {
                        LoadResult.Error(exception)
                    }
                } else {
                    return try {
                        val episodesEntity =
                            episodesDao.getPagingList()
                                .map { episodesEntity ->
                                    EpisodesModel(
                                        id = episodesEntity.id,
                                        name = episodesEntity.name,
                                        airDate = episodesEntity.airDate,
                                        episode = episodesEntity.episode,
                                        charactersList = episodesEntity.characters.charactersList
                                    )
                                }
                        LoadResult.Page(
                            data = episodesEntity,
                            prevKey = if (position == 0) null else position.minus(1),
                            nextKey = if (position == 0) null else position.plus(1)
                        )

                    } catch (exception: Exception) {
                        LoadResult.Error(exception)
                    }
                }
            }

            override fun getRefreshKey(state: PagingState<Int, EpisodesModel>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }

        }
    }
}