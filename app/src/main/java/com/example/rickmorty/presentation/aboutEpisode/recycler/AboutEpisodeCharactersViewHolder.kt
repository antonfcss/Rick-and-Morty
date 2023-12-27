package com.example.rickmorty.presentation.aboutEpisode.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.EpisodeCharacterItemBinding

class AboutEpisodeCharactersViewHolder(
    private val binding: EpisodeCharacterItemBinding,
    private val onClick: (Int) -> Unit,
) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(aboutEpisodeCharactersModel: AboutEpisodeCharactersModel) {
        with(binding) {
            root.setOnClickListener { onClick.invoke(aboutEpisodeCharactersModel.characterId) }
            avatarImageView.setImageBitmap(aboutEpisodeCharactersModel.image)
            nameTextView.text = aboutEpisodeCharactersModel.characterName
            speciesTextView.text = aboutEpisodeCharactersModel.characterSpecies
            statusTextView.text = aboutEpisodeCharactersModel.characterStatus
            genderTextView.text = aboutEpisodeCharactersModel.characterGender

        }
    }
}