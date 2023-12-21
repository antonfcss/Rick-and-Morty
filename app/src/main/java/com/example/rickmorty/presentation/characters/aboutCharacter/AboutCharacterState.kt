package com.example.rickmorty.presentation.characters.aboutCharacter

import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterRecyclerModel

data class AboutCharacterState(
    val characterModelsList: List<AboutCharacterRecyclerModel>,
)