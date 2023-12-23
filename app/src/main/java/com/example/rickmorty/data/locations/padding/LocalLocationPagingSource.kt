package com.example.rickmorty.data.locations.padding

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.domain.locations.LocationModel

class LocalLocationPagingSource(
    private val locationsDao: LocationsDao,
    private val name: String?,
    private val type: String?,
    private val dimension: String?
) : PagingSource<Int, LocationModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationModel> {
        val position = params.key ?: 0
        return try {
            val locationsEntities = when {
                name != null -> locationsDao.searchByName(name)
                type != null -> locationsDao.searchByType(type)
                dimension != null -> locationsDao.searchByDimension(dimension)
                else -> locationsDao.getPagingList()
            }.drop(position * params.loadSize)
                .take(params.loadSize)

            val locationsModelList = locationsEntities.map { locationEntity ->
                LocationModel(
                    id = locationEntity.id,
                    name = locationEntity.name,
                    type = locationEntity.type,
                    dimension = locationEntity.dimension,
                    residents = locationEntity.residents.residentsList
                )
            }

            LoadResult.Page(
                data = locationsModelList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (locationsEntities.size < params.loadSize) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocationModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}