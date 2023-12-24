package com.example.rickmorty.data.characters.paging

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickmorty.R
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.domain.characters.CharactersModel
import com.example.rickmorty.domain.characters.LocationModel
import com.example.rickmorty.domain.characters.OriginModel
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException

class LocalCharacterPagingSource(
    private val charactersDao: CharactersDao,
    private val context: Context,
    private val name: String?,
    private val status: String?,
    private val species: String?
) : PagingSource<Int, CharactersModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersModel> {
        val position = params.key ?: 0
        return try {
            val characterEntities = when {
                name != null -> charactersDao.searchByName(name)
                status != null -> charactersDao.searchByStatus(status)
                species != null -> charactersDao.searchBySpecies(species)
                else -> charactersDao.getPagingList()
            }.drop(position * params.loadSize)
                .take(params.loadSize)

            val charactersModelList = characterEntities.map { characterEntity ->
                CharactersModel(
                    id = characterEntity.id,
                    name = characterEntity.name,
                    status = characterEntity.status,
                    species = characterEntity.species,
                    type = characterEntity.type,
                    gender = characterEntity.gender,
                    origin = OriginModel(
                        name = characterEntity.origin?.name ?: "",
                        id = characterEntity.origin?.id ?: 0,
                    ),
                    location = LocationModel(
                        name = characterEntity.location?.name ?: "",
                        id = characterEntity.location?.id ?: 0
                    ),
                    image = loadImageFromStorage(characterEntity.id),
                    episode = characterEntity.episode.episodesList
                )
            }

            LoadResult.Page(
                data = charactersModelList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (characterEntities.size < params.loadSize) null else position + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharactersModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun loadImageFromStorage(id: Int): Bitmap {
        return try {
            val path = "/data/data/${context.packageName}/app_imageDir/$id.jpg"
            val file = File(path)
            BitmapFactory.decodeStream(FileInputStream(file))
        } catch (e: FileNotFoundException) {
            BitmapFactory.decodeResource(context.resources, R.drawable.placeholder_image)
                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
        }
    }
}