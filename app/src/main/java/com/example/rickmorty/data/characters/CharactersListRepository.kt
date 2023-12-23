package com.example.rickmorty.data.characters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rickmorty.R
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.local.entities.CharacterEntity
import com.example.rickmorty.data.characters.local.entities.CharacterEpisodesEntity
import com.example.rickmorty.data.characters.local.entities.CharacterLocationEntity
import com.example.rickmorty.data.characters.local.entities.OriginEntity
import com.example.rickmorty.data.characters.paging.CharactersPagingSource
import com.example.rickmorty.data.characters.paging.LocalCharacterPagingSource
import com.example.rickmorty.domain.characters.CharactersModel
import com.example.rickmorty.domain.characters.LocationModel
import com.example.rickmorty.domain.characters.OriginModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject

class CharactersListRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val context: Context,
    private val internetManager: InternetManager,
    private val charactersDao: CharactersDao,
    private val charactersPagingSource: CharactersPagingSource,
    private val scope: CoroutineScope
) {

    fun getPagingCharacters(
        name: String?,
        status: String?,
        species: String?,
    ): Flow<PagingData<CharactersModel>> {
        val pagingSourceFactory: () -> PagingSource<Int, CharactersModel> = {
            if (internetManager.isInternetConnected()) {
                charactersPagingSource.getPagingCharacters(
                    name = name,
                    status = status,
                    species = species
                )
            } else {
                LocalCharacterPagingSource(charactersDao, context, name, status, species)
            }
        }

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow.cachedIn(scope)
    }
    suspend fun getAboutCharacterFromApi(id: Int): Flow<CharactersModel> {
        return if (internetManager.isInternetConnected()) {
            flow {
                val apiAboutCharacter = api.getAboutCharacter(id)
                if (charactersDao.searchById(id) == null) {
                    val imagePath = saveToInternalStorage(
                        getImageFromRemote(context, apiAboutCharacter.image),
                        id
                    )
                    charactersDao.insertAll(
                        listOf(
                            CharacterEntity(
                                id = apiAboutCharacter.id,
                                name = apiAboutCharacter.name,
                                status = apiAboutCharacter.status,
                                species = apiAboutCharacter.species,
                                type = apiAboutCharacter.type,
                                gender = apiAboutCharacter.gender,
                                origin = OriginEntity(
                                    name = apiAboutCharacter.originApi.originName,
                                    id = apiAboutCharacter.locationApi.url.extractLastPartToIntOrZero()
                                ),
                                location = CharacterLocationEntity(
                                    name = apiAboutCharacter.locationApi.locationName,
                                    id = apiAboutCharacter.locationApi.url.extractLastPartToIntOrZero()
                                ),
                                image = imagePath,
                                episode = CharacterEpisodesEntity(
                                    apiAboutCharacter.episode.map { it.extractLastPartToIntOrZero() }
                                )
                            )))
                }
                emit(
                    CharactersModel(
                        id = apiAboutCharacter.id,
                        name = apiAboutCharacter.name,
                        status = apiAboutCharacter.status,
                        species = apiAboutCharacter.species,
                        type = apiAboutCharacter.type,
                        gender = apiAboutCharacter.gender,
                        origin = if (apiAboutCharacter.originApi.url.isNotEmpty()) {
                            OriginModel(
                                name = apiAboutCharacter.originApi.originName,
                                id = apiAboutCharacter.locationApi.url.extractLastPartToIntOrZero()
                            )
                        } else null,
                        location = if (apiAboutCharacter.locationApi.url.isNotEmpty()) {
                            LocationModel(
                                name = apiAboutCharacter.locationApi.locationName,
                                id = apiAboutCharacter.locationApi.url.extractLastPartToIntOrZero()
                            )
                        } else null,
                        image = loadImageFromStorage(id),
                        episode = apiAboutCharacter.episode.map { it.extractLastPartToIntOrZero() }
                    )
                )
            }
        } else {
            val characterEntity = charactersDao.searchById(id)
            characterEntity?.let {
                flow {
                    emit(
                        CharactersModel(
                            id = it.id,
                            name = it.name,
                            status = it.status,
                            species = it.species,
                            type = it.type,
                            gender = it.gender,
                            origin = OriginModel(
                                name = it.origin?.name ?: "",
                                id = it.origin?.id ?: 0,
                            ),
                            location = LocationModel(
                                name = it.location?.name ?: "",
                                id = it.location?.id ?: 0
                            ),
                            image = loadImageFromStorage(it.id),
                            episode = it.episode.episodesList
                        )
                    )
                }
            } ?: flow {
                throw Exception("We're having trouble with the database Character")
            }
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
