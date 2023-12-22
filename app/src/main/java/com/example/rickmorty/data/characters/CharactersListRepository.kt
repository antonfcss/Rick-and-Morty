package com.example.rickmorty.data.characters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickmorty.R
import com.example.rickmorty.base.extractLastPartToIntOrZero
import com.example.rickmorty.data.InternetManager
import com.example.rickmorty.data.RickAndMortyApi
import com.example.rickmorty.data.characters.local.CharactersDao
import com.example.rickmorty.data.characters.paging.CharactersPagingSource
import com.example.rickmorty.domain.characters.CharactersModel
import com.example.rickmorty.domain.characters.LocationModel
import com.example.rickmorty.domain.characters.OriginModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
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

    fun getPagingCharacters(
        name: String?,
        status: String?,
        species: String?,
    ): Flow<PagingData<CharactersModel>> {
        if (internetManager.isInternetConnected()) {
            return Pager(config = PagingConfig(pageSize = 20, enablePlaceholders = false),
                pagingSourceFactory = {
                    charactersPagingSource.getPagingCharacters(
                        name = name,
                        status = status,
                        species = species
                    )
                }).flow.cachedIn(
                scope
            )
        } else {
            val filteredCharactersList = {
                when {
                    name != null -> charactersDao.searchByName(name)
                    status != null -> charactersDao.searchByStatus(status)
                    species != null -> charactersDao.searchBySpecies(species)
                    else -> charactersDao.getPagingList()
                }
            }.asFlow()
            return filteredCharactersList.flatMapConcat { characterEntities ->
                flow {
                    emit(
                        PagingData.from(characterEntities.map { characterEntity ->
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
                        })
                    )
                }
            }

        }
    }

    suspend fun getAboutCharacterFromApi(id: Int): Flow<CharactersModel> {
        return flow {
            val apiAboutCharacter = api.getAboutCharacter(id)
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