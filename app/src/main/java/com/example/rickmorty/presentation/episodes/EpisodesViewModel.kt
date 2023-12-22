package com.example.rickmorty.presentation.episodes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.map
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.episodes.EpisodesUseCase
import com.example.rickmorty.presentation.episodes.dialog.EpisodeFilters
import com.example.rickmorty.presentation.episodes.recycler.EpisodesUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class EpisodesViewModel @Inject constructor(private val episodesUseCase: EpisodesUseCase) :
    BaseViewModel<EpisodesState>() {
    private var filterState = EpisodeFilters.NAME
    private val filterLiveData = MutableLiveData<EpisodeFilters?>()
    fun getFilterLiveData(): LiveData<EpisodeFilters?> = filterLiveData
    fun onFilterButtonClicked() {
        filterLiveData.postValue(filterState)
    }

    fun postFilterClicked() {
        filterLiveData.postValue(null)
    }

    fun setFilter(filter: EpisodeFilters) {
        filterState = filter
    }

    fun getEpisodesList() {
        loadEpisodesList(name = null, episode = null)
    }

    fun getEpisodesListByQuery(searchText: String) {
        loadEpisodesList(
            name = searchText.takeIf { filterState == EpisodeFilters.NAME },
            episode = searchText.takeIf { filterState == EpisodeFilters.EPISODE },
        )
    }

    private fun loadEpisodesList(name: String?, episode: String?) {
        launchIO {
            episodesUseCase.getPagingEpisodes(name = name, episode = episode)
                .catch { Log.d("EpisodesViewModel", it.message.toString()) }
                .map { pagingData ->
                    pagingData.map { episodesModel ->
                        EpisodesUiModel(
                            id = episodesModel.id,
                            name = episodesModel.name,
                            airDate = episodesModel.airDate,
                            episode = episodesModel.dateEpisode
                        )
                    }
                }
                .collect { episodesList ->
                    updateState(
                        ViewState.Success(
                            EpisodesState(
                                episodesList
                            )
                        )
                    )
                }
        }
    }
}