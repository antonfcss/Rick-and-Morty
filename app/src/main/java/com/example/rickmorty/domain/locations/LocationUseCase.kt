package com.example.rickmorty.domain.locations

import com.example.rickmorty.data.locations.LocationsRepository
import javax.inject.Inject

class LocationUseCase @Inject constructor(private val locationsRepository: LocationsRepository) {

    suspend fun getPagingLocations() = locationsRepository.getPagingLocations()
}