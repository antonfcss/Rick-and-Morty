package com.example.rickmorty.presentation.characters.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.RecyclerCharacterItemBinding

class CharactersViewHolder(private val binding: RecyclerCharacterItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(charactersUiModel: CharactersUiModel, clickListener: (Int) -> Unit) {
        binding.avatarImageView.setImageBitmap(charactersUiModel.image)
        binding.nameTextView.text = charactersUiModel.characterName
        binding.speciesTextView.text = charactersUiModel.characterSpecies
        binding.statusTextView.text = charactersUiModel.characterStatus
        binding.genderTextView.text = charactersUiModel.characterGender
        binding.root.setOnClickListener { clickListener.invoke(charactersUiModel.id) }
    }
}