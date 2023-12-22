package com.example.rickmorty.presentation.characters.aboutCharacter.recycler

import android.view.View
import com.example.rickmorty.databinding.CharacterDetailItemBinding
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterUiModel

class AboutCharacterDetailViewHolder(
    private val binding: CharacterDetailItemBinding,
    private val onLocationClicked: (Int) -> Unit,
    private val onOriginClicked: (Int) -> Unit,
) : AboutCharacterViewHolder(binding) {
    fun bind(aboutCharacterUiModel: AboutCharacterUiModel) {
        with(binding) {
            nameCharactersTextView.text = aboutCharacterUiModel.characterName
            statusTextView.text = aboutCharacterUiModel.status
            speciesTextView.text = aboutCharacterUiModel.species
            if (aboutCharacterUiModel.type.isNotEmpty()) {
                typeTextView.text = aboutCharacterUiModel.type
            } else {
                typeTextView.visibility = View.GONE
            }
            genderTextView.text = aboutCharacterUiModel.gender
            if (aboutCharacterUiModel.originName.isNullOrEmpty().not()) {
                originTextView.text = aboutCharacterUiModel.originName
                originTextView.setOnClickListener {
                    aboutCharacterUiModel.originId?.let { it1 -> onOriginClicked.invoke(it1) }
                }
            } else {
                originTextView.visibility = View.GONE
                originTitleTextView.visibility = View.GONE
            }
            locationsTextView.text = aboutCharacterUiModel.locationName
            locationsTextView.setOnClickListener {
                onLocationClicked.invoke(aboutCharacterUiModel.locationId)
            }
            avatarImageView.setImageBitmap(aboutCharacterUiModel.image)
        }
    }
}