package com.example.rickmorty.presentation.characters.aboutCharacter.recycler

import androidx.recyclerview.widget.DiffUtil
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.model.AboutCharacterRecyclerModel

class AboutCharacterDiffUtil : DiffUtil.ItemCallback<AboutCharacterRecyclerModel>() {
    override fun areItemsTheSame(
        oldItem: AboutCharacterRecyclerModel,
        newItem: AboutCharacterRecyclerModel
    ): Boolean {
        return oldItem::javaClass == newItem::javaClass
    }

    override fun areContentsTheSame(
        oldItem: AboutCharacterRecyclerModel,
        newItem: AboutCharacterRecyclerModel
    ): Boolean {
        return oldItem == newItem
    }
}