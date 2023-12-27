package com.example.rickmorty.data.locations.padding

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.data.locations.local.LocationsDao
import com.example.rickmorty.data.locations.local.entities.LocationEntity

class LocalLocationPagingSource(
    private val locationsDao: LocationsDao,
    private val name: String?,
    private val type: String?,
    private val dimension: String?
) : PagingSource<Int, LocationEntity>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationEntity> {
        val locationsEntities = try {
            when {
                name != null -> locationsDao.searchByName(name)
                type != null -> locationsDao.searchByType(type)
                dimension != null -> locationsDao.searchByDimension(dimension)
                else -> locationsDao.getPagingList()
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        return locationsEntities.load(params)
    }

    override fun getRefreshKey(state: PagingState<Int, LocationEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}