package com.example.rickmorty.data.episodes.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.data.episodes.local.EpisodesDao
import com.example.rickmorty.data.episodes.local.entities.EpisodesEntity

class LocalEpisodePagingSource(
    private val episodesDao: EpisodesDao,
    private val name: String?,
    private val episode: String?
) : PagingSource<Int, EpisodesEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesEntity> {
        val episodesEntities = when {
            name != null -> episodesDao.searchByName(name)
            episode != null -> episodesDao.searchByEpisode(episode)
            else -> episodesDao.getPagingList()
        }
        return episodesEntities.load(params)
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodesEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}