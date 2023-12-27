package com.example.rickmorty.presentation.aboutCharacter.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.rickmorty.presentation.aboutCharacter.recycler.model.AboutCharacterEpisodeModel
import com.example.rickmorty.presentation.aboutCharacter.recycler.model.AboutCharacterRecyclerModel
import com.example.rickmorty.presentation.aboutCharacter.recycler.model.AboutCharacterUiModel

class AboutCharacterDiffUtil : DiffUtil.ItemCallback<AboutCharacterRecyclerModel>() {
    override fun areItemsTheSame(
        oldItem: AboutCharacterRecyclerModel,
        newItem: AboutCharacterRecyclerModel
    ): Boolean {
        return when {
            oldItem is AboutCharacterUiModel && newItem is AboutCharacterUiModel ->
                oldItem.locationId == newItem.locationId

            oldItem is AboutCharacterEpisodeModel && newItem is AboutCharacterEpisodeModel ->
                oldItem.episodeId == newItem.episodeId

            else -> false
        }
    }

    override fun areContentsTheSame(
        oldItem: AboutCharacterRecyclerModel,
        newItem: AboutCharacterRecyclerModel
    ): Boolean {
        return oldItem == newItem
    }
}
