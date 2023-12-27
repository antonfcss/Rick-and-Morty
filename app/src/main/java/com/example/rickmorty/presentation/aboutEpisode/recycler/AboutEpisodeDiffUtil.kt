package com.example.rickmorty.presentation.aboutEpisode.recycler

import androidx.recyclerview.widget.DiffUtil

class AboutEpisodeDiffUtil : DiffUtil.ItemCallback<AboutEpisodeCharactersModel>() {
    override fun areItemsTheSame(
        oldItem: AboutEpisodeCharactersModel,
        newItem: AboutEpisodeCharactersModel
    ): Boolean {
        return oldItem.characterId == newItem.characterId
    }

    override fun areContentsTheSame(
        oldItem: AboutEpisodeCharactersModel,
        newItem: AboutEpisodeCharactersModel
    ): Boolean {
        return oldItem == newItem
    }
}