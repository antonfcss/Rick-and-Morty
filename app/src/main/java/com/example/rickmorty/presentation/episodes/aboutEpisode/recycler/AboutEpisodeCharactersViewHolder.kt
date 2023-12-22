package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler

import com.example.rickmorty.databinding.EpisodeCharacterItemBinding
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeCharactersModel

class AboutEpisodeCharactersViewHolder(private val binding: EpisodeCharacterItemBinding) :
    AboutEpisodeViewHolder(binding) {

    fun bind(aboutEpisodeCharactersModel: AboutEpisodeCharactersModel) {
        with(binding) {
            avatarImageView.setImageBitmap(aboutEpisodeCharactersModel.image)
            nameTextView.text = aboutEpisodeCharactersModel.characterName
            speciesTextView.text = aboutEpisodeCharactersModel.characterSpecies
            statusTextView.text = aboutEpisodeCharactersModel.characterStatus
            genderTextView.text = aboutEpisodeCharactersModel.characterGender
        }
    }
}