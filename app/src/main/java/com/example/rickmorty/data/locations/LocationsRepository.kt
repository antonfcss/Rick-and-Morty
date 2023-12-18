package com.example.rickmorty.data.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.data.locations.padding.LocationPagingSource
import com.example.rickmorty.domain.locations.LocationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val locationPagingSource: LocationPagingSource

) {

    fun getPagingLocations(): Flow<PagingData<LocationModel>> {
        return Pager(config = PagingConfig(pageSize = 2, enablePlaceholders = false),
            pagingSourceFactory = { locationPagingSource.getPagingLocations() }).flow.cachedIn(scope)
    }
}