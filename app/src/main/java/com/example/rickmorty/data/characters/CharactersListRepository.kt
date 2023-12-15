package com.example.rickmorty.data.characters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.rickmorty.R
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.local.entities.CharacterEntity
import com.example.rickmorty.data.characters.local.entities.EpisodesEntity
import com.example.rickmorty.data.characters.local.entities.LocationEntity
import com.example.rickmorty.data.characters.local.entities.OriginEntity
import com.example.rickmorty.domain.characters.CharactersModel
import com.example.rickmorty.domain.characters.LocationModel
import com.example.rickmorty.domain.characters.OriginModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class CharactersListRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val context: Context,
    private val internetManager: InternetManager,
    private val charactersDao: CharactersDao,
) {

    suspend fun getCharactersList() = flow {
        if (internetManager.isInternetConnected()) {
            emit(getDataFromApi())
        } else {
            emit(getDataFromDatabase())
        }
    }


    private suspend fun getDataFromApi(): List<CharactersModel> {
        val apiCharactersModelList =
            api.getCharacterList().charactersResultsList
        val charactersModelList = apiCharactersModelList.map { charactersApiModel ->
            CharactersModel(
                id = charactersApiModel.id,
                name = charactersApiModel.name,
                status = charactersApiModel.status,
                species = charactersApiModel.species,
                type = charactersApiModel.type,
                gender = charactersApiModel.gender,
                origin = OriginModel(
                    name = charactersApiModel.origin.originName,
                    url = charactersApiModel.origin.url,
                ),
                location = LocationModel(
                    name = charactersApiModel.location.locationName,
                    url = charactersApiModel.location.url
                ),
                image = getImageFromRemote(context, charactersApiModel.image),
                episode = charactersApiModel.episode,
            )
        }
        charactersDao.insertAll(charactersModelList.map { charactersApiModel ->
            val path = saveToInternalStorage(charactersApiModel.image, charactersApiModel.id ?: 0)
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
                location = LocationEntity(
                    name = charactersApiModel.location.name,
                    url = charactersApiModel.location.url
                ),
                image = path,
                episode = EpisodesEntity(charactersApiModel.episode),
            )
        })
        return charactersModelList
    }

    private suspend fun getDataFromDatabase(): List<CharactersModel> {
        return charactersDao.getAll().map { characterEntity ->
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
                    name = characterEntity.location.name, url = characterEntity.location.url
                ),
                image = loadImageFromStorage(characterEntity.id),
                episode = characterEntity.episode.episodesList,
            )
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
            } catch (e: IOException) {
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
                Log.e("NewsRepository", "Error loading image: ${e.message}")
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