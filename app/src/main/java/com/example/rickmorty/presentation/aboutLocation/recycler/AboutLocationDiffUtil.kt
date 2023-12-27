package com.example.rickmorty.presentation.aboutLocation.recycler

import androidx.recyclerview.widget.DiffUtil

class AboutLocationDiffUtil : DiffUtil.ItemCallback<AboutLocationCharactersModel>() {
    override fun areItemsTheSame(
        oldItem: AboutLocationCharactersModel,
        newItem: AboutLocationCharactersModel
    ): Boolean {
        return oldItem.characterId == newItem.characterId
    }

    override fun areContentsTheSame(
        oldItem: AboutLocationCharactersModel,
        newItem: AboutLocationCharactersModel
    ): Boolean {
        return oldItem == newItem
    }
}