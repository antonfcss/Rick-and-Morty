package com.example.rickmorty.presentation.characters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.rickmorty.databinding.RecyclerCharacterItemBinding

class CharactersAdapter(
    private val onClick: (Int) -> Unit
) :
    PagingDataAdapter<CharactersUiModel, CharactersViewHolder>(CharactersDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            RecyclerCharacterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClick) }
    }
}