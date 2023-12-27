package com.example.rickmorty.data.episodes.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.episodes.api.EpisodesApi
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.local.entities.EpisodesCharacterEntity
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity
import com.example.rickmorty.domain.episodes.EpisodesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EpisodesPagingSource @Inject constructor(
    private val episodesDao: EpisodesDao,
    private val episodesApi: EpisodesApi,
    private val name: String?,
    private val episode: String?
) : PagingSource<Int, EpisodesModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesModel> {
        val position = params.key ?: 0
        val response = try {
            episodesApi.getEpisodesList(
                position + 1,
                name = name,
                episode = episode
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        val episodesModelList =
            response.episodesApiModels.map { episodesApiModel ->
                EpisodesModel(
                    id = episodesApiModel.id,
                    name = episodesApiModel.name,
                    airDate = episodesApiModel.airDate,
                    dateEpisode = episodesApiModel.episode,
                    charactersList = episodesApiModel.characters.map { it.extractLastPartToIntOrZero() }
                )
            }
        withContext(Dispatchers.IO) {
            episodesDao.insertAll(episodesModelList.map { episodesModel ->
                EpisodesEntity(
                    id = episodesModel.id,
                    name = episodesModel.name,
                    airDate = episodesModel.airDate,
                    episode = episodesModel.dateEpisode,
                    characters = EpisodesCharacterEntity(episodesModel.charactersList)
                )
            })
        }
        return if (episodesModelList.isEmpty()) {
            LoadResult.Error(Throwable("Empty data"))
        } else {
            LoadResult.Page(
                data = episodesModelList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (response.episodesApiModels.isEmpty()) null else position + 1
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodesModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}