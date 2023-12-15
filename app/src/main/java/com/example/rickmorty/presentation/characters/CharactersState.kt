package com.example.rickmorty.presentation.characters

import com.example.rickmorty.presentation.characters.recycler.CharactersUiModel

data class CharactersState(
    val charactersList: List<CharactersUiModel>
)