package com.example.rickmorty.domain.locations

import com.example.rickmorty.data.locations.LocationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationUseCase @Inject constructor(private val locationsRepository: LocationsRepository) {

    fun getPagingLocations(
        name: String?,
        type: String?,
        dimension: String?
    ) = locationsRepository.getPagingLocations(name = name, type = type, dimension = dimension)

    suspend fun getAboutLocation(id: Int): Flow<LocationModel> {
        return locationsRepository.getAboutLocationFromApi(id)
    }
}