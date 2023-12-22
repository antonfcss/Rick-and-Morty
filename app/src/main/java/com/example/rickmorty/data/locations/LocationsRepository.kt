package com.example.rickmorty.data.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.padding.LocationPagingSource
import com.example.rickmorty.domain.locations.LocationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationsRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val locationPagingSource: LocationPagingSource,
    private val api: RickAndMortyApi,
    private val internetManager: InternetManager,
    private val locationsDao: LocationsDao,
) {

    fun getPagingLocations(
        name: String?,
        type: String?,
        dimension: String?
    ): Flow<PagingData<LocationModel>> {
        if (internetManager.isInternetConnected()) {
            return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = {
                    locationPagingSource.getPagingLocations(
                        name = name,
                        type = type,
                        dimension = dimension
                    )
                }).flow.cachedIn(
                scope
            )
        } else {
            val filterLocationList = {
                when {
                    name != null -> locationsDao.searchByName(name)
                    type != null -> locationsDao.searchByType(type)
                    dimension != null -> locationsDao.searchByDimension(dimension)
                    else -> locationsDao.getPagingList()
                }
            }.asFlow()
            return filterLocationList.flatMapConcat { locationsEntities ->
                flow {
                    emit(PagingData.from(locationsEntities.map { locationEntity ->
                        LocationModel(
                            id = locationEntity.id,
                            name = locationEntity.name,
                            type = locationEntity.type,
                            dimension = locationEntity.dimension,
                            residents = locationEntity.residents.residentsList
                        )
                    }))
                }
            }
        }
    }

    suspend fun getAboutLocationFromApi(id: Int): Flow<LocationModel> {
        return flow {
            val apiAboutLocation = api.getAboutLocation(id)
            emit(
                LocationModel(
                    id = apiAboutLocation.id,
                    name = apiAboutLocation.name,
                    type = apiAboutLocation.type,
                    dimension = apiAboutLocation.dimension,
                    residents = apiAboutLocation.residents.map { it.extractLastPartToIntOrZero() }
                )
            )
        }
    }
}