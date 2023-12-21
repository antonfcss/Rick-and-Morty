package com.example.rickmorty.presentation.characters.aboutCharacter.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.rickmorty.databinding.CharacterDetailItemBinding
import com.example.rickmorty.databinding.CharacterEpisodesItemBinding
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterEpisodeModel
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterRecyclerModel
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterUiModel

class AboutCharacterAdapter(
    private val onLocationClicked: (Int) -> Unit,
) :
    ListAdapter<AboutCharacterRecyclerModel, AboutCharacterViewHolder>(AboutCharacterDiffUtil()) {

    private companion object {
        const val ABOUT_CHARACTER_DETAIL = 0
        const val ABOUT_CHARACTER_EPISODES = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ABOUT_CHARACTER_DETAIL
        } else {
            ABOUT_CHARACTER_EPISODES
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutCharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ABOUT_CHARACTER_DETAIL -> AboutCharacterDetailViewHolder(
                CharacterDetailItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                ),
                onLocationClicked
            )

            ABOUT_CHARACTER_EPISODES -> AboutCharacterEpisodesViewHolder(
                CharacterEpisodesItemBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw IllegalArgumentException("Ошибка ViewType")
        }
    }

    override fun onBindViewHolder(holder: AboutCharacterViewHolder, position: Int) {
        when (holder) {
            is AboutCharacterDetailViewHolder -> holder.bind(getItem(position) as AboutCharacterUiModel)
            is AboutCharacterEpisodesViewHolder -> holder.bind(getItem(position) as AboutCharacterEpisodeModel)
        }
    }
}