package com.example.rickmorty.presentation.locations.aboutLocation

import android.util.Log
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.locations.AboutLocationInteract
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationCharactersModel
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationDetailModel
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationRecyclerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class AboutLocationViewModel @Inject constructor(
    private val aboutLocationInteract: AboutLocationInteract
) : BaseViewModel<AboutLocationState>() {

    fun getDetailAboutLocation(id: Int) {
        launchIO {
            aboutLocationInteract.getDetailAboutLocation(id)
                .catch { Log.d("AboutLocationViewModel", it.message.toString()) }
                .collect { aboutLocationModel ->
                    val locationUiModelList = arrayListOf<AboutLocationRecyclerModel>()
                    val locationModel = AboutLocationDetailModel(
                        name = aboutLocationModel.name,
                        type = aboutLocationModel.type,
                        dimension = aboutLocationModel.dimension
                    )
                    locationUiModelList.add(locationModel)
                    val charactersList = aboutLocationModel.charactersList.map { characte ->
                        AboutLocationCharactersModel(
                            characterName = characte.name,
                            characterSpecies = characte.species,
                            characterStatus = characte.status,
                            characterGender = characte.gender,
                            image = characte.image
                        )
                    }
                    locationUiModelList.addAll(charactersList)
                    updateState(ViewState.Success(AboutLocationState(locationUiModelList)))
                }
        }
    }
}