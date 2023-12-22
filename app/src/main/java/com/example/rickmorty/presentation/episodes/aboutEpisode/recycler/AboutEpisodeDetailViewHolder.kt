package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler

import com.example.rickmorty.databinding.EpisodeDetailItemBinding
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeDetailModel

class AboutEpisodeDetailViewHolder(private val binding: EpisodeDetailItemBinding) :
    AboutEpisodeViewHolder(binding) {
    fun bind(aboutEpisodeDetailModel: AboutEpisodeDetailModel) {
        with(binding) {
            nameEpisodeTextView.text = aboutEpisodeDetailModel.nameEpisode
            airDataEpisodeTextView.text = aboutEpisodeDetailModel.airDate
            numberEpisodeTextView.text = aboutEpisodeDetailModel.dateEpisode
        }
    }
}