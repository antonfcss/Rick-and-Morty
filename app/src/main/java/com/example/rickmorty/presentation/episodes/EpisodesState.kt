package com.example.rickmorty.presentation.episodes

import androidx.paging.PagingData
import com.example.rickmorty.presentation.episodes.recycler.EpisodesUiModel

data class EpisodesState(
    val episodesList: PagingData<EpisodesUiModel>
)