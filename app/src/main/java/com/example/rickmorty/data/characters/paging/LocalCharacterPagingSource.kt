package com.example.rickmorty.data.characters.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.local.entities.CharacterEntity

class LocalCharacterPagingSource(
    private val charactersDao: CharactersDao,
    private val name: String?,
    private val status: String?,
    private val species: String?
) : PagingSource<Int, CharacterEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterEntity> {
        val characterEntities = when {
            name != null -> charactersDao.searchByName(name)
            status != null -> charactersDao.searchByStatus(status)
            species != null -> charactersDao.searchBySpecies(species)
            else -> charactersDao.getPagingList()
        }
        return characterEntities.load(params)
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}