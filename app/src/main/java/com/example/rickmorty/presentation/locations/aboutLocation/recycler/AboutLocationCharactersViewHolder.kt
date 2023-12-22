package com.example.rickmorty.presentation.locations.aboutLocation.recycler

import com.example.rickmorty.databinding.LocationCharacterItemBinding
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationCharactersModel

class AboutLocationCharactersViewHolder(private val binding: LocationCharacterItemBinding) :
    AboutLocationViewHolder(binding) {
    fun bind(aboutLocationCharactersModel: AboutLocationCharactersModel) {
        with(binding) {
            avatarImageView.setImageBitmap(aboutLocationCharactersModel.image)
            nameTextView.text = aboutLocationCharactersModel.characterName
            speciesTextView.text = aboutLocationCharactersModel.characterSpecies
            statusTextView.text = aboutLocationCharactersModel.characterStatus
            genderTextView.text = aboutLocationCharactersModel.characterGender
        }
    }
}