package com.example.rickmorty.presentation.characters.aboutCharacter.recycler

import com.example.rickmorty.databinding.CharacterEpisodesItemBinding
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterEpisodeModel

class AboutCharacterEpisodesViewHolder(private val binding: CharacterEpisodesItemBinding) :
    AboutCharacterViewHolder(binding) {
    fun bind(aboutCharacterEpisodeModel: AboutCharacterEpisodeModel) {
        with(binding) {
            nameEpisodeTextView.text = aboutCharacterEpisodeModel.nameEpisode
            numberEpisodeTextView.text = aboutCharacterEpisodeModel.numberEpisode
            airDataEpisodeTextView.text = aboutCharacterEpisodeModel.airDataEpisode
        }
    }
}