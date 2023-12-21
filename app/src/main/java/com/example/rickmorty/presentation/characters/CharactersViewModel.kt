package com.example.rickmorty.presentation.characters

import android.util.Log
import androidx.paging.map
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.characters.CharactersUseCase
import com.example.rickmorty.presentation.characters.dialog.CharacterFilters
import com.example.rickmorty.presentation.characters.recycler.CharactersUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val charactersUseCase: CharactersUseCase) :
    BaseViewModel<CharactersState>() {

    private var filterState = CharacterFilters.NAME

    fun getFilter(): CharacterFilters {
        return filterState
    }

    fun setFilter(filter: CharacterFilters) {
        filterState = filter
    }


    fun getCharactersList() {
        loadCharactersList(
            name = null,
            status = null,
            species = null,
        )
    }

    fun getCharactersListByQuery(searchText: String) {
        loadCharactersList(
            name = searchText.takeIf { filterState == CharacterFilters.NAME },
            status = searchText.takeIf { filterState == CharacterFilters.STATUS },
            species = searchText.takeIf { filterState == CharacterFilters.SPECIES },
        )
    }

    private fun loadCharactersList(
        name: String?,
        status: String?,
        species: String?,
    ) {
        launchIO {
            charactersUseCase.getPagingCharacters(
                name = name,
                status = status,
                species = species,
            )
                .catch { Log.d("CharactersViewModel", it.message.toString()) }
                .map { pagingData ->
                    pagingData.map { charactersModel ->
                        CharactersUiModel(
                            id = charactersModel.id,
                            characterName = charactersModel.name,
                            characterSpecies = charactersModel.species,
                            characterStatus = charactersModel.status,
                            characterGender = charactersModel.gender,
                            image = charactersModel.image
                        )
                    }
                }
                .collect { charactersList ->
                    updateState(ViewState.Success(CharactersState(charactersList)))
                }
        }
    }
}
