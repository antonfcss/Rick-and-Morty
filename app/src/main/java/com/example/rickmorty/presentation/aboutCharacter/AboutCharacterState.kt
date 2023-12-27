package com.example.rickmorty.presentation.aboutCharacter

import com.example.rickmorty.presentation.aboutCharacter.recycler.model.AboutCharacterRecyclerModel

data class AboutCharacterState(
    val characterModelsList: List<AboutCharacterRecyclerModel>,
)