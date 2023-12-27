package com.example.rickmorty.presentation.aboutLocation

import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.locations.AboutLocationInteractor
import com.example.rickmorty.presentation.aboutLocation.recycler.AboutLocationCharactersModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class AboutLocationViewModel @Inject constructor(
    private val aboutLocationInteractor: AboutLocationInteractor
) : BaseViewModel<AboutLocationState>() {

    fun getDetailAboutLocation(id: Int) {
        launchIO {
            aboutLocationInteractor.getDetailAboutLocation(id)
                .catch { updateState(ViewState.Error(it.message.toString())) }
                .collect { aboutLocationModel ->
                    val charactersList = aboutLocationModel.charactersList.map { characte ->
                        AboutLocationCharactersModel(
                            characterId = characte.id,
                            characterName = characte.name,
                            characterSpecies = characte.species,
                            characterStatus = characte.status,
                            characterGender = characte.gender,
                            image = characte.image
                        )
                    }
                    updateState(
                        ViewState.Success(
                            AboutLocationState(
                                name = aboutLocationModel.name,
                                type = aboutLocationModel.type,
                                dimension = aboutLocationModel.dimension,
                                charactersList
                            )
                        )
                    )
                }
        }
    }
}