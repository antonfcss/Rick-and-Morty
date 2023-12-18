package com.example.rickmorty.presentation.characters

import androidx.paging.PagingData
import com.example.rickmorty.presentation.characters.recycler.CharactersUiModel

data class CharactersState(
    val charactersList: PagingData<CharactersUiModel>
)