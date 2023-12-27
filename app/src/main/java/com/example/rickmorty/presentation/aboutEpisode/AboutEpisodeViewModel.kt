package com.example.rickmorty.presentation.aboutEpisode

import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.episodes.AboutEpisodeInteractor
import com.example.rickmorty.presentation.aboutEpisode.recycler.AboutEpisodeCharactersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class AboutEpisodeViewModel @Inject constructor(private val aboutEpisodeInteractor: AboutEpisodeInteractor) :
    BaseViewModel<AboutEpisodeState>() {

    fun getAboutEpisodeDetail(id: Int) {
        launchIO {
            aboutEpisodeInteractor.getDetailAboutEpisode(id)
                .catch { updateState(ViewState.Error(it.message.toString())) }
                .collect { aboutEpisodeModel ->
                    val charactersList = aboutEpisodeModel.charactersList.map { character ->
                        AboutEpisodeCharactersModel(
                            characterId = character.id,
                            characterName = character.name,
                            characterSpecies = character.species,
                            characterStatus = character.status,
                            characterGender = character.gender,
                            image = character.image
                        )
                    }
                    updateState(
                        ViewState.Success(
                            AboutEpisodeState(
                                nameEpisode = aboutEpisodeModel.nameEpisode,
                                airDate = aboutEpisodeModel.airDate,
                                dateEpisode = aboutEpisodeModel.dateEpisode,
                                charactersList
                            )
                        )
                    )
                }
        }
    }

}