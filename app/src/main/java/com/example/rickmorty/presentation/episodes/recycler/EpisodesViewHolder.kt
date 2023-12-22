package com.example.rickmorty.presentation.episodes.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.RecyclerEpisodeItemBinding

class EpisodesViewHolder(private val binding: RecyclerEpisodeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(episodesUiModel: EpisodesUiModel, clickListener: (Int) -> Unit) {
        with(binding) {
            nameEpisodeTextView.text = episodesUiModel.name
            numberEpisodeTextView.text = episodesUiModel.episode
            airDataEpisodeTextView.text = episodesUiModel.airDate
            root.setOnClickListener { clickListener.invoke(episodesUiModel.id) }
        }
    }
}