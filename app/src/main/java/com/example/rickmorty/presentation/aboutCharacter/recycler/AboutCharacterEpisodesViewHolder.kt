package com.example.rickmorty.presentation.aboutCharacter.recycler

import com.example.rickmorty.databinding.CharacterEpisodesItemBinding
import com.example.rickmorty.presentation.aboutCharacter.recycler.model.AboutCharacterEpisodeModel

class AboutCharacterEpisodesViewHolder(
    private val binding: CharacterEpisodesItemBinding,
    private val onEpisodeClicked: (Int) -> Unit,
) :
    AboutCharacterViewHolder(binding) {
    fun bind(aboutCharacterEpisodeModel: AboutCharacterEpisodeModel) {
        with(binding) {
            nameEpisodeTextView.text = aboutCharacterEpisodeModel.nameEpisode
            numberEpisodeTextView.text = aboutCharacterEpisodeModel.numberEpisode
            airDataEpisodeTextView.text = aboutCharacterEpisodeModel.airDataEpisode
            root.setOnClickListener {
                onEpisodeClicked.invoke(aboutCharacterEpisodeModel.episodeId)
            }
        }
    }
}