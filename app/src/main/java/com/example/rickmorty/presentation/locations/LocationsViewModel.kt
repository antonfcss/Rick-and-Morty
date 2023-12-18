package com.example.rickmorty.presentation.locations

import android.util.Log
import androidx.paging.map
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.locations.LocationUseCase
import com.example.rickmorty.presentation.locations.recycler.LocationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(private val locationUseCase: LocationUseCase) :
    BaseViewModel<LocationsState>() {

    fun getLocationList() {
        launchIO {
            locationUseCase.getPagingLocations()
                .catch { Log.d("LocationsViewModel", it.message.toString()) }
                .map { pagingData ->
                    pagingData.map { locationsModel ->
                        LocationUiModel(
                            id = locationsModel.id,
                            name = locationsModel.name,
                            type = locationsModel.type,
                            dimension = locationsModel.dimension,
                        )
                    }

                }
                .collect { locationsList ->
                    updateState(
                        ViewState.Success(
                            LocationsState(locationsList)
                        )
                    )
                }
        }
    }

}