package com.example.rickmorty.data.characters.paging

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseSource
import com.example.rickmorty.base.Results
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
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
import okio.IOException
import retrofit2.HttpException
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject

class CharactersPagingSource @Inject constructor(
    private val baseSource: BaseSource,
    private val context: Context,
    private val internetManager: InternetManager,
    private val charactersDao: CharactersDao,
    private val api: RickAndMortyApi
) : BaseSource by baseSource {

    fun getPagingCharacters(): PagingSource<Int, CharactersModel> {
        return object : PagingSource<Int, CharactersModel>() {


            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharactersModel> {
                val position = params.key ?: 0
                if (internetManager.isInternetConnected()) {
                    return try {
                        when (val response = oneShotCalls { api.getCharacterList(position * 2) }) {
                            is Results.Success -> {
                                val charactersModelList =
                                    response.data.charactersResultsList.map { charactersApiModel ->
                                        CharactersModel(
                                            id = charactersApiModel.id,
                                            name = charactersApiModel.name,
                                            status = charactersApiModel.status,
                                            species = charactersApiModel.species,
                                            type = charactersApiModel.type,
                                            gender = charactersApiModel.gender,
                                            origin = OriginModel(
                                                name = charactersApiModel.originApi.originName,
                                                url = charactersApiModel.originApi.url,
                                            ),
                                            location = LocationModel(
                                                name = charactersApiModel.locationApi.locationName,
                                                url = charactersApiModel.locationApi.url
                                            ),
                                            image = getImageFromRemote(
                                                context, charactersApiModel.image
                                            ),
                                            episode = charactersApiModel.episode
                                        )
                                    }
                                withContext(Dispatchers.IO) {
                                    charactersDao.insertAll(charactersModelList.map { charactersApiModel ->
                                        val path = saveToInternalStorage(
                                            charactersApiModel.image, charactersApiModel.id ?: 0
                                        )
                                        CharacterEntity(
                                            id = charactersApiModel.id ?: 0,
                                            name = charactersApiModel.name,
                                            status = charactersApiModel.status,
                                            species = charactersApiModel.species,
                                            type = charactersApiModel.type,
                                            gender = charactersApiModel.gender,
                                            origin = OriginEntity(
                                                name = charactersApiModel.origin.name,
                                                url = charactersApiModel.origin.url
                                            ),
                                            location = CharacterLocationEntity(
                                                name = charactersApiModel.location.name,
                                                url = charactersApiModel.location.url
                                            ),
                                            image = path,
                                            episode = CharacterEpisodesEntity(charactersApiModel.episode),
                                        )
                                    })
                                }
                                if (charactersModelList.isEmpty()) {
                                    LoadResult.Error(Throwable("Empty data"))
                                } else {
                                    LoadResult.Page(
                                        data = charactersModelList,
                                        prevKey = if (position == 0) null else position - 1,
                                        nextKey = if (response.data.charactersResultsList.isEmpty()) null else position + 1
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
                        val charactersEntity =
                            charactersDao.getPagingList(params.loadSize, offset = position * 2)
                                .map { characterEntity ->
                                    CharactersModel(
                                        id = characterEntity.id,
                                        name = characterEntity.name,
                                        status = characterEntity.status,
                                        species = characterEntity.species,
                                        type = characterEntity.type,
                                        gender = characterEntity.gender,
                                        origin = OriginModel(
                                            name = characterEntity.origin.name,
                                            url = characterEntity.origin.url,
                                        ),
                                        location = LocationModel(
                                            name = characterEntity.location.name,
                                            url = characterEntity.location.url
                                        ),
                                        image = loadImageFromStorage(characterEntity.id),
                                        episode = characterEntity.episode.episodesList,
                                    )
                                }
                        LoadResult.Page(
                            data = charactersEntity,
                            prevKey = if (position == 0) null else position.minus(1),
                            nextKey = if (position == 0) null else position.plus(1)
                        )
                    } catch (exception: Exception) {
                        LoadResult.Error(exception)
                    }
                }
            }

            override fun getRefreshKey(state: PagingState<Int, CharactersModel>): Int? {
                return state.anchorPosition?.let { anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                        ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
                }
            }
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
