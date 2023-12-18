package com.example.rickmorty.presentation.locations

import androidx.paging.PagingData
import com.example.rickmorty.presentation.locations.recycler.LocationUiModel

data class LocationsState(
    val locationList: PagingData<LocationUiModel>
)