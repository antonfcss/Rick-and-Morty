package com.example.rickmorty.presentation.characters.aboutCharacter

import android.util.Log
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.characters.AboutCharacterInteractor
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterEpisodeModel
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterRecyclerModel
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class AboutCharacterViewModel @Inject constructor(private val aboutCharacterInteractor: AboutCharacterInteractor) :
    BaseViewModel<AboutCharacterState>() {

    fun getAboutCharacterDetail(id: Int) {
        launchIO {
            aboutCharacterInteractor.getDetailAboutCharacter(id)
                .catch { Log.d("AboutCharacterViewModel", it.message.toString()) }
                .collect { aboutCharacterModel ->
                    val characterUiModelList = arrayListOf<AboutCharacterRecyclerModel>()
                    val characterModel = AboutCharacterUiModel(
                        characterName = aboutCharacterModel.name,
                        status = aboutCharacterModel.status,
                        species = aboutCharacterModel.species,
                        type = aboutCharacterModel.type,
                        gender = aboutCharacterModel.gender,
                        originName = aboutCharacterModel.originName,
                        locationId = aboutCharacterModel.locationId,
                        locationName = aboutCharacterModel.locationName,
                        image = aboutCharacterModel.image,
                    )
                    characterUiModelList.add(characterModel)
                    val episodesList = aboutCharacterModel.episodeList.map { episode ->
                        AboutCharacterEpisodeModel(
                            nameEpisode = episode.name,
                            numberEpisode = episode.episode,
                            airDataEpisode = episode.airDate,
                        )
                    }
                    characterUiModelList.addAll(episodesList)
                    updateState(
                        ViewState.Success(
                            AboutCharacterState(
                                characterModelsList = characterUiModelList
                            )
                        )
                    )
                }
        }
    }
}