package com.example.rickmorty.data.characters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.R
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.paging.CharactersPagingSource
import com.example.rickmorty.domain.characters.CharactersModel
import com.example.rickmorty.domain.characters.LocationModel
import com.example.rickmorty.domain.characters.OriginModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import javax.inject.Inject

class CharactersListRepository @Inject constructor(
    private val api: RickAndMortyApi,
    private val context: Context,
    private val internetManager: InternetManager,
    private val charactersDao: CharactersDao,
    private val charactersPagingSource: CharactersPagingSource,
    private val scope: CoroutineScope

) {

    fun getPagingCharacters(): Flow<PagingData<CharactersModel>> {
        return Pager(config = PagingConfig(pageSize = 2, enablePlaceholders = false),
            pagingSourceFactory = { charactersPagingSource.getPagingCharacters() }).flow.cachedIn(
            scope
        )
    }

    suspend fun getAboutCharacterFromApi(id: Int): Flow<CharactersModel> {
        return flow {
            val apiAboutCharacter = api.getAboutCharacter(id).body()!!
            val characterModel = CharactersModel(
                id = apiAboutCharacter.id,
                name = apiAboutCharacter.name,
                status = apiAboutCharacter.status,
                species = apiAboutCharacter.species,
                type = apiAboutCharacter.type,
                gender = apiAboutCharacter.gender,
                origin = OriginModel(
                    name = apiAboutCharacter.originApi.originName,
                    url = apiAboutCharacter.originApi.url
                ),
                location = LocationModel(
                    name = apiAboutCharacter.locationApi.locationName,
                    url = apiAboutCharacter.locationApi.url
                ),
                image = loadImageFromStorage(id),
                episode = apiAboutCharacter.episode
            )
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
//    suspend fun getCharactersList() = flow {
//        if (internetManager.isInternetConnected()) {
//            emit(getDataFromApi())
//        } else {
//            emit(getDataFromDatabase())
//        }
//    }


//    private suspend fun getDataFromApi(): List<CharactersModel> {
//        val apiCharactersModelList =
//            api.getCharacterList().charactersResultsList
//        val charactersModelList = apiCharactersModelList.map { charactersApiModel ->
//            CharactersModel(
//                id = charactersApiModel.id,
//                name = charactersApiModel.name,
//                status = charactersApiModel.status,
//                species = charactersApiModel.species,
//                type = charactersApiModel.type,
//                gender = charactersApiModel.gender,
//                origin = OriginModel(
//                    name = charactersApiModel.origin.originName,
//                    url = charactersApiModel.origin.url,
//                ),
//                location = LocationModel(
//                    name = charactersApiModel.location.locationName,
//                    url = charactersApiModel.location.url
//                ),
//                image = getImageFromRemote(context, charactersApiModel.image),
//                episode = charactersApiModel.episode
//            )
//        }
//        charactersDao.insertAll(charactersModelList.map { charactersApiModel ->
//            val path = saveToInternalStorage(charactersApiModel.image, charactersApiModel.id ?: 0)
//            CharacterEntity(
//                id = charactersApiModel.id ?: 0,
//                name = charactersApiModel.name,
//                status = charactersApiModel.status,
//                species = charactersApiModel.species,
//                type = charactersApiModel.type,
//                gender = charactersApiModel.gender,
//                origin = OriginEntity(
//                    name = charactersApiModel.origin.name,
//                    url = charactersApiModel.origin.url
//                ),
//                location = LocationEntity(
//                    name = charactersApiModel.location.name,
//                    url = charactersApiModel.location.url
//                ),
//                image = path,
//                episode = EpisodesEntity(charactersApiModel.episode),
//            )
//        })
//        return charactersModelList
//    }
//
//    private suspend fun getDataFromDatabase(): List<CharactersModel> {
//        return charactersDao.getPagingList().map { characterEntity ->
//            CharactersModel(
//                id = characterEntity.id,
//                name = characterEntity.name,
//                status = characterEntity.status,
//                species = characterEntity.species,
//                type = characterEntity.type,
//                gender = characterEntity.gender,
//                origin = OriginModel(
//                    name = characterEntity.origin.name,
//                    url = characterEntity.origin.url,
//                ),
//                location = LocationModel(
//                    name = characterEntity.location.name, url = characterEntity.location.url
//                ),
//                image = loadImageFromStorage(characterEntity.id),
//                episode = characterEntity.episode.episodesList,
//            )
//        }
//    }
//
//    private fun saveToInternalStorage(bitmapImage: Bitmap, id: Int): String {
//        val directory: File = context.getDir("imageDir", Context.MODE_PRIVATE)
//        val myPath = File(directory, "$id.jpg")
//        var fileOutputStream: FileOutputStream? = null
//        try {
//            fileOutputStream = FileOutputStream(myPath)
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            try {
//                fileOutputStream?.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return myPath.absolutePath
//    }
//
//    private suspend fun getImageFromRemote(context: Context, frontImage: String): Bitmap {
//        return withContext(Dispatchers.IO) {
//            try {
//                val requestOptions = RequestOptions()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                val bitmap = Glide.with(context)
//                    .applyDefaultRequestOptions(requestOptions)
//                    .asBitmap()
//                    .load(frontImage)
//                    .submit()
//                    .get()
//                bitmap
//            } catch (e: Exception) {
//                Log.e("NewsRepository", "Error loading image: ${e.message}")
//                null
//            }
//        } ?: run {
//            val placeholderDrawable =
//                ContextCompat.getDrawable(context, R.drawable.placeholder_image)
//            val bitmap = Bitmap.createBitmap(
//                placeholderDrawable!!.intrinsicWidth,
//                placeholderDrawable.intrinsicHeight,
//                Bitmap.Config.ARGB_8888
//            )
//            val canvas = Canvas(bitmap)
//            placeholderDrawable.setBounds(0, 0, canvas.width, canvas.height)
//            placeholderDrawable.draw(canvas)
//            bitmap
//        }
//    }
//
//    private fun loadImageFromStorage(id: Int): Bitmap {
//        return try {
//            val path = "/data/data/${context.packageName}/app_imageDir/$id.jpg"
//            val file = File(path)
//            BitmapFactory.decodeStream(FileInputStream(file))
//        } catch (e: FileNotFoundException) {
//            BitmapFactory.decodeResource(context.resources, R.drawable.placeholder_image)
//                ?: Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565)
//        }
//    }
}