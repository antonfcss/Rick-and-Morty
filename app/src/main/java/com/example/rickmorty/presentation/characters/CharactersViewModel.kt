package com.example.rickmorty.presentation.characters

import android.util.Log
import androidx.paging.map
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.characters.CharactersUseCase
import com.example.rickmorty.presentation.characters.recycler.CharactersUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val charactersUseCase: CharactersUseCase) :
    BaseViewModel<CharactersState>() {

    fun getCharactersList() {
        launchIO {
            charactersUseCase.getPagingCharacters()
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