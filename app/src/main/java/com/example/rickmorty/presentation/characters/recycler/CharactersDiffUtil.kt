package com.example.rickmorty.presentation.characters.recycler

import androidx.recyclerview.widget.DiffUtil

class CharactersDiffUtil : DiffUtil.ItemCallback<CharactersUiModel>() {
    override fun areItemsTheSame(oldItem: CharactersUiModel, newItem: CharactersUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: CharactersUiModel,
        newItem: CharactersUiModel
    ): Boolean {
        return oldItem == newItem
    }
}