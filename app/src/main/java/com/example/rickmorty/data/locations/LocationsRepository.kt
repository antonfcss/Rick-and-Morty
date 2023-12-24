package com.example.rickmorty.data.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.local.entities.LocationEntity
import com.example.rickmorty.data.locations.local.entities.ResidentsEntity
import com.example.rickmorty.data.locations.padding.LocalLocationPagingSource
import com.example.rickmorty.data.locations.padding.LocationPagingSource
import com.example.rickmorty.domain.locations.LocationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
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
        val pagingSourceFactory: () -> PagingSource<Int, LocationModel> = {
            if (internetManager.isInternetConnected()) {
                locationPagingSource.getPagingLocations(
                    name = name,
                    type = type,
                    dimension = dimension
                )
            } else {
                LocalLocationPagingSource(locationsDao, name, type, dimension)
            }
        }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow.cachedIn(scope)
    }

    suspend fun getAboutLocationFromApi(id: Int): Flow<LocationModel> {
        return if (internetManager.isInternetConnected()) {
            flow {
                try {
                    val apiAboutLocation = api.getAboutLocation(id)
                    if (locationsDao.searchById(id) == null) {
                        locationsDao.insertAll(
                            listOf(
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
                        )
                    }
                    emit(
                        LocationModel(
                            id = apiAboutLocation.id,
                            name = apiAboutLocation.name,
                            type = apiAboutLocation.type,
                            dimension = apiAboutLocation.dimension,
                            residents = apiAboutLocation.residents.map { it.extractLastPartToIntOrZero() }
                        )
                    )
                } catch (e: Exception) {
                    throw Exception("Failed to fetch location from API", e)
                }
            }
        } else {
            val locationEntity = locationsDao.searchById(id)
            return if (locationEntity != null) {
                flow {
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
            } else {
                flow {
                    throw Exception("Location not found in the database")
                }
            }
        }
    }
}