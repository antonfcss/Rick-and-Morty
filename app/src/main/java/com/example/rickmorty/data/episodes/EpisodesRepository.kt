package com.example.rickmorty.data.episodes

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.data.episodes.paging.EpisodesPagingSource
import com.example.rickmorty.domain.episodes.EpisodesModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EpisodesRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val episodesPagingSource: EpisodesPagingSource
) {

    fun getPagingEpisodes(): Flow<PagingData<EpisodesModel>> {
        return Pager(config = PagingConfig(pageSize = 2, enablePlaceholders = false),
            pagingSourceFactory = { episodesPagingSource.getPagingEpisodes() }).flow.cachedIn(scope)
    }
}