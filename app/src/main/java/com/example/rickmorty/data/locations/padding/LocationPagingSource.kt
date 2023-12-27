package com.example.rickmorty.data.locations.padding

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.locations.api.LocationsApi
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.local.entities.LocationEntity
import com.example.rickmorty.data.locations.local.entities.ResidentsEntity
import com.example.rickmorty.domain.locations.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationPagingSource @Inject constructor(
    private val locationsDao: LocationsDao,
    private val locationsApi: LocationsApi,
    private val name: String?,
    private val type: String?,
    private val dimension: String?
) : PagingSource<Int, LocationModel>() {

    override fun getRefreshKey(state: PagingState<Int, LocationModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModel> {
        val position = params.key ?: 0
        val response = try {
            locationsApi.getLocationsList(
                position + 1, name = name, type = type, dimension = dimension
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        val locationsModelList = response.locationsApiModels.map { locationsApiModel ->
            LocationModel(id = locationsApiModel.id,
                name = locationsApiModel.name,
                type = locationsApiModel.type,
                dimension = locationsApiModel.dimension,
                residents = locationsApiModel.residents.map { it.extractLastPartToIntOrZero() })
        }
        withContext(Dispatchers.IO) {
            locationsDao.insertAll(locationsModelList.map { locationModel ->
                LocationEntity(
                    id = locationModel.id,
                    name = locationModel.name,
                    type = locationModel.type,
                    dimension = locationModel.dimension,
                    residents = ResidentsEntity(locationModel.residents)
                )
            })
        }
        return if (locationsModelList.isEmpty()) {
            LoadResult.Error(Exception("EmptyData"))
        } else {
            LoadResult.Page(
                data = locationsModelList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (response.locationsApiModels.isEmpty()) null else position + 1
            )
        }
    }
}
