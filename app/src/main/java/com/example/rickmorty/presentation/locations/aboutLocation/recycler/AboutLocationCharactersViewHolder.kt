package com.example.rickmorty.presentation.locations.aboutLocation.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.LocationCharacterItemBinding

class AboutLocationCharactersViewHolder(
    private val binding: LocationCharacterItemBinding,
    private val onClick: (Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(aboutLocationCharactersModel: AboutLocationCharactersModel) {
        with(binding) {
            avatarImageView.setImageBitmap(aboutLocationCharactersModel.image)
            nameTextView.text = aboutLocationCharactersModel.characterName
            speciesTextView.text = aboutLocationCharactersModel.characterSpecies
            statusTextView.text = aboutLocationCharactersModel.characterStatus
            genderTextView.text = aboutLocationCharactersModel.characterGender
            root.setOnClickListener {
                onClick.invoke(aboutLocationCharactersModel.characterId)
            }
        }
    }
}