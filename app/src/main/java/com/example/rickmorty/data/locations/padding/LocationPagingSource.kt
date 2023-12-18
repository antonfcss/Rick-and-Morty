package com.example.rickmorty.data.locations.padding

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.base.BaseSource
import com.example.rickmorty.base.Results
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.local.entities.LocationEntity
import com.example.rickmorty.data.locations.local.entities.ResidentsEntity
import com.example.rickmorty.domain.locations.LocationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LocationPagingSource @Inject constructor(
    private val baseSource: BaseSource,
    private val locationsDao: LocationsDao,
    private val api: RickAndMortyApi,
    private val internetManager: InternetManager,
) : BaseSource by baseSource {

    fun getPagingLocations(): PagingSource<Int, LocationModel> {
        return object : PagingSource<Int, LocationModel>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModel> {
                val position = params.key ?: 0
                if (internetManager.isInternetConnected()) {
                    return try {
                        when (val response = oneShotCalls { api.getLocationsList(position * 2) }) {
                            is Results.Success -> {
                                val locationsModelList =
                                    response.data.locationsApiModels.map { locationsApiModel ->
                                        LocationModel(
                                            id = locationsApiModel.id,
                                            name = locationsApiModel.name,
                                            type = locationsApiModel.type,
                                            dimension = locationsApiModel.dimension,
                                            residents = locationsApiModel.residents
                                        )
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
                                if (locationsModelList.isEmpty()) {
                                    LoadResult.Error(Throwable("Empty data"))
                                } else {
                                    LoadResult.Page(
                                        data = locationsModelList,
                                        prevKey = if (position == 0) null else position - 1,
                                        nextKey = if (response.data.locationsApiModels.isEmpty()) null else position + 1
                                    )
                                }
                            }

                            is Results.Error -> {
                                LoadResult.Error(response.exception)
                            }
                        }
                    } catch (exception: IOException) {
                        LoadResult.Error(exception)
                    } catch (exception: HttpException) {
                        LoadResult.Error(exception)
                    }
                } else {
                    return try {
                        val locationsEntity =
                            locationsDao.getPagingList(params.loadSize, offset = position * 2)
                                .map { locationEntity ->
                                    LocationModel(
                                        id = locationEntity.id,
                                        name = locationEntity.name,
                                        type = locationEntity.type,
                                        dimension = locationEntity.dimension,
                                        residents = locationEntity.residents.residentsList
                                    )
                                }
                        LoadResult.Page(
                            data = locationsEntity,
                            prevKey = if (position == 0) null else position.minus(1),
                            nextKey = if (position == 0) null else position.plus(1)
                        )
                    } catch (exception: Exception) {
                        LoadResult.Error(exception)
                    }
                }
            }

            override fun getRefreshKey(state: PagingState<Int, LocationModel>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
        }
    }
}