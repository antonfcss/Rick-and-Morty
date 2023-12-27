package com.example.rickmorty.data.characters.paging

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rickmorty.R
import com.example.rickmorty.base.extractLastPartToInt
import com.example.rickmorty.data.characters.api.CharactersApi
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.local.entities.CharacterEntity
import com.example.rickmorty.data.characters.local.entities.CharacterEpisodesEntity
import com.example.rickmorty.data.characters.local.entities.CharacterLocationEntity
import com.example.rickmorty.data.characters.local.entities.OriginEntity
import com.example.rickmorty.domain.characters.CharactersModel
import com.example.rickmorty.domain.characters.LocationModel
import com.example.rickmorty.domain.characters.OriginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class CharactersPagingSource @Inject constructor(
    private val charactersApi: CharactersApi,
    private val context: Context,
    private val charactersDao: CharactersDao,
    private val name: String?,
    private val status: String?,
    private val species: String?
) : PagingSource<Int, CharactersModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersModel> {
        val position = params.key ?: 0
        val response = try {
            charactersApi.getCharacterList(
                page = position + 1,
                name = name,
                status = status,
                species = species,
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
        val charactersModelList =
            response.charactersResultsList.map { charactersApiModel ->
                CharactersModel(
                    id = charactersApiModel.id,
                    name = charactersApiModel.name,
                    status = charactersApiModel.status,
                    species = charactersApiModel.species,
                    type = charactersApiModel.type,
                    gender = charactersApiModel.gender,
                    origin = if (charactersApiModel.originApi.url.isNotEmpty()) {
                        OriginModel(
                            name = charactersApiModel.originApi.originName,
                            id = charactersApiModel.originApi.url.extractLastPartToInt(),
                        )
                    } else null,
                    location = if (charactersApiModel.locationApi.url.isNotEmpty()) {
                        LocationModel(
                            name = charactersApiModel.locationApi.locationName,
                            id = charactersApiModel.locationApi.url.extractLastPartToInt()
                        )
                    } else null,
                    image = getImageFromRemote(
                        context, charactersApiModel.image
                    ),
                    episode = charactersApiModel.episode.map {
                        it.extractLastPartToInt()
                    }
                )
            }
        withContext(Dispatchers.IO) {
            charactersDao.insertAll(charactersModelList.map { characterModel ->
                val path = saveToInternalStorage(
                    characterModel.image, characterModel.id
                )
                CharacterEntity(
                    id = characterModel.id,
                    name = characterModel.name,
                    status = characterModel.status,
                    species = characterModel.species,
                    type = characterModel.type,
                    gender = characterModel.gender,
                    origin = OriginEntity(
                        name = characterModel.origin?.name ?: "",
                        id = characterModel.origin?.id ?: 0
                    ),
                    location = CharacterLocationEntity(
                        name = characterModel.location?.name ?: "",
                        id = characterModel.location?.id ?: 0
                    ),
                    image = path,
                    episode = CharacterEpisodesEntity(characterModel.episode),
                )
            })
        }
        return if (charactersModelList.isEmpty()) {
            LoadResult.Error(Throwable("Empty data"))
        } else {
            LoadResult.Page(
                data = charactersModelList,
                prevKey = if (position == 0) null else position - 1,
                nextKey = if (response.charactersResultsList.isEmpty()) null else position + 1
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharactersModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap, id: Int): String {
        val directory: File = context.getDir("imageDir", Context.MODE_PRIVATE)
        val myPath = File(directory, "$id.jpg")
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = FileOutputStream(myPath)
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fileOutputStream?.close()
            } catch (e: java.io.IOException) {
                e.printStackTrace()
            }
        }
        return myPath.absolutePath
    }

    private suspend fun getImageFromRemote(context: Context, frontImage: String): Bitmap {
        return withContext(Dispatchers.IO) {
            try {
                val requestOptions = RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                val bitmap = Glide.with(context)
                    .applyDefaultRequestOptions(requestOptions)
                    .asBitmap()
                    .load(frontImage)
                    .submit()
                    .get()
                bitmap
            } catch (e: Exception) {
                null
            }
        } ?: run {
            val placeholderDrawable =
                ContextCompat.getDrawable(context, R.drawable.placeholder_image)
            val bitmap = Bitmap.createBitmap(
                placeholderDrawable!!.intrinsicWidth,
                placeholderDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            placeholderDrawable.setBounds(0, 0, canvas.width, canvas.height)
            placeholderDrawable.draw(canvas)
            bitmap
        }
    }

}
