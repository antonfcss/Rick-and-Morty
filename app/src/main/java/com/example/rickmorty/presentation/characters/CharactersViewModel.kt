package com.example.rickmorty.presentation.characters

import android.util.Log
import com.example.rickmorty.base.BaseViewModel
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.domain.characters.CharactersUseCase
import com.example.rickmorty.presentation.characters.recycler.CharactersUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(private val charactersUseCase: CharactersUseCase) :
    BaseViewModel<CharactersState>() {

    fun getCharactersList() {
        launchIO {
            charactersUseCase.getCharactersList()
                .catch { Log.d("CharactersViewModel", it.message.toString()) }
                .collect { charactersList ->
                    val arrayList = arrayListOf<CharactersUiModel>()
                    charactersList.forEach { charactersModel ->
                        Log.d("CharactersViewModel", "Received charactersList: $charactersList")
                        arrayList.add(
                            CharactersUiModel(
                                id = charactersModel.id,
                                characterName = charactersModel.name,
                                characterSpecies = charactersModel.species,
                                characterStatus = charactersModel.status,
                                characterGender = charactersModel.gender,
                                image = charactersModel.image
                            )
                        )

                    }
                    updateState(ViewState.Success(CharactersState(arrayList)))
                }
        }
    }
}