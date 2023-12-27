package com.example.rickmorty.presentation.episodes.recycler

import androidx.recyclerview.widget.DiffUtil

class EpisodesDiffUtil : DiffUtil.ItemCallback<EpisodesUiModel>() {

    override fun areItemsTheSame(oldItem: EpisodesUiModel, newItem: EpisodesUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EpisodesUiModel, newItem: EpisodesUiModel): Boolean {
        return oldItem == newItem
    }
}