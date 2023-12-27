package com.example.rickmorty.data.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.locations.api.LocationsApi
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.local.entities.LocationEntity
import com.example.rickmorty.data.locations.local.entities.ResidentsEntity
import com.example.rickmorty.data.locations.padding.LocalLocationPagingSource
import com.example.rickmorty.data.locations.padding.LocationPagingSource
import com.example.rickmorty.domain.locations.LocationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val internetManager: InternetManager,
    private val locationsDao: LocationsDao,
    private val locationsApi: LocationsApi,
) {

    fun getPagingLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<LocationModel>> = if (internetManager.isInternetConnected()) {
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                LocationPagingSource(
                    locationsDao,
                    locationsApi,
                    name,
                    type,
                    dimension
                )
            }
        ).flow.cachedIn(scope)
    } else {
        Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = {
                LocalLocationPagingSource(
                    locationsDao,
                    name,
                    type,
                    dimension
                )
            }
        ).flow.map {
            it.map { locationEntity ->
                LocationModel(
                    id = locationEntity.id,
                    name = locationEntity.name,
                    type = locationEntity.type,
                    dimension = locationEntity.dimension,
                    residents = locationEntity.residents.residentsList
                )
            }
        }.cachedIn(scope)
    }

    suspend fun getAboutLocationFromApi(id: Int): Flow<LocationModel> = flow {
        if (internetManager.isInternetConnected()) {
            val apiAboutLocation = locationsApi.getAboutLocation(id)
            locationsDao.insert(
                LocationEntity(
                    id = apiAboutLocation.id,
                    name = apiAboutLocation.name,
                    type = apiAboutLocation.type,
                    dimension = apiAboutLocation.dimension,
                    residents = ResidentsEntity(
                        apiAboutLocation.residents.map { it.extractLastPartToIntOrZero() }
                    )
                )
            )
            emit(
                LocationModel(
                    id = apiAboutLocation.id,
                    name = apiAboutLocation.name,
                    type = apiAboutLocation.type,
                    dimension = apiAboutLocation.dimension,
                    residents = apiAboutLocation.residents.map { it.extractLastPartToIntOrZero() }
                )
            )
        } else {
            val locationEntity = locationsDao.searchById(id)
            locationEntity?.let {
                emit(
                    LocationModel(
                        id = locationEntity.id,
                        name = locationEntity.name,
                        type = locationEntity.type,
                        dimension = locationEntity.dimension,
                        residents = locationEntity.residents.residentsList
                    )
                )
            }
        }
    }
}