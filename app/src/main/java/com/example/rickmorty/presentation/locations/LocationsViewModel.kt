package com.example.rickmorty.presentation.locations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.map
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.locations.LocationUseCase
import com.example.rickmorty.presentation.locations.diaolog.LocationFilters
import com.example.rickmorty.presentation.locations.recycler.LocationUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(private val locationUseCase: LocationUseCase) :
    BaseViewModel<LocationsState>() {
    private var filterState = LocationFilters.NAME
    private val filterLiveData = MutableLiveData<LocationFilters?>()

    fun getFilterLiveData(): LiveData<LocationFilters?> = filterLiveData
    fun onFilterButtonClicked() {
        filterLiveData.postValue(filterState)
    }

    fun postFilterClicked() {
        filterLiveData.postValue(null)
    }

    fun setFilter(filter: LocationFilters) {
        filterState = filter
    }

    fun getLocationList() {
        loadLocationList(name = null, type = null, dimension = null)
    }

    fun getLocationsListByQuery(searchText: String) {
        loadLocationList(
            name = searchText.takeIf { filterState == LocationFilters.NAME },
            type = searchText.takeIf { filterState == LocationFilters.TYPE },
            dimension = searchText.takeIf { filterState == LocationFilters.DIMENSION },
            )
    }

    private fun loadLocationList(
        name: String?,
        type: String?,
        dimension: String?
    ) {
        launchIO {
            locationUseCase.getPagingLocations(name = name, type = type, dimension = dimension)
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

    fun onEmptyDataReceiver() {
        showEmptyDataDialog("Received an empty list of locations")
    }

}