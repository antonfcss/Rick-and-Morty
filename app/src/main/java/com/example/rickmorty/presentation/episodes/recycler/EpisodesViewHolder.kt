package com.example.rickmorty.presentation.episodes.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.RecyclerEpisodeItemBinding

class EpisodesViewHolder(private val binding: RecyclerEpisodeItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(episodesUiModel: EpisodesUiModel) {
        binding.nameEpisodeTextView.text = episodesUiModel.name
        binding.numberEpisodeTextView.text = episodesUiModel.episode
        binding.airDataEpisodeTextView.text = episodesUiModel.airDate
    }
}