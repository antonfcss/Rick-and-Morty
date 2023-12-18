package com.example.rickmorty.presentation.episodes

import android.util.Log
import androidx.paging.map
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.episodes.EpisodesUseCase
import com.example.rickmorty.presentation.episodes.recycler.EpisodesUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(private val episodesUseCase: EpisodesUseCase) :
    BaseViewModel<EpisodesState>() {
    fun getEpisodesList() {
        launchIO {
            episodesUseCase.getPagingEpisodes()
                .catch { Log.d("EpisodesViewModel", it.message.toString()) }
                .map { pagingData ->
                    pagingData.map { episodesModel ->
                        EpisodesUiModel(
                            id = episodesModel.id,
                            name = episodesModel.name,
                            airDate = episodesModel.airDate,
                            episode = episodesModel.episode
                        )
                    }
                }
                .collect { episodesList -> updateState(ViewState.Success(EpisodesState(episodesList))) }
        }
    }
}