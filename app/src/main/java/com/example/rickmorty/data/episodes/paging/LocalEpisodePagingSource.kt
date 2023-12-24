package com.example.rickmorty.data.episodes.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.domain.episodes.EpisodesModel

class LocalEpisodePagingSource(
    private val episodesDao: EpisodesDao,
    private val name: String?,
    private val episode: String?
) : PagingSource<Int, EpisodesModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesModel> {
        val position = params.key ?: 0
        return try {
            val episodesEntities = when {
                name != null -> episodesDao.searchByName(name)
                episode != null -> episodesDao.searchByEpisode(episode)
                else -> episodesDao.getPagingList()
            }.drop(position * params.loadSize)
                .take(params.loadSize)

            val episodesModelList = episodesEntities.map { episodesEntity ->
                EpisodesModel(
                    id = episodesEntity.id,
                    name = episodesEntity.name,
                    airDate = episodesEntity.airDate,
                    dateEpisode = episodesEntity.episode,
                    charactersList = episodesEntity.characters.charactersList
                )
            }

            LoadResult.Page(
                data = episodesModelList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (episodesEntities.size < params.loadSize) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodesModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}