package com.example.rickmorty.presentation.episodes.aboutEpisode

import android.util.Log
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.episodes.AboutEpisodeInteract
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeCharactersModel
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeDetailModel
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeRecyclerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class AboutEpisodeViewModel @Inject constructor(private val aboutEpisodeInteract: AboutEpisodeInteract) :
    BaseViewModel<AboutEpisodeState>() {

    fun getAboutEpisodeDetail(id: Int) {
        launchIO {
            aboutEpisodeInteract.getDetailAboutEpisode(id)
                .catch { Log.d("AboutEpisodeViewModel", it.message.toString()) }
                .collect { aboutEpisodeModel ->
                    val episodeUiModelList = arrayListOf<AboutEpisodeRecyclerModel>()
                    val episodeModel = AboutEpisodeDetailModel(
                        nameEpisode = aboutEpisodeModel.nameEpisode,
                        airDate = aboutEpisodeModel.airDate,
                        dateEpisode = aboutEpisodeModel.dateEpisode,
                    )
                    episodeUiModelList.add(episodeModel)
                    val charactersList = aboutEpisodeModel.charactersList.map { character ->
                        AboutEpisodeCharactersModel(
                            characterName = character.name,
                            characterSpecies = character.species,
                            characterStatus = character.status,
                            characterGender = character.gender,
                            image = character.image
                        )
                    }
                    episodeUiModelList.addAll(charactersList)
                    updateState(ViewState.Success(AboutEpisodeState(episodeUiModelList)))
                }
        }
    }

}