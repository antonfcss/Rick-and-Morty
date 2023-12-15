package com.example.rickmorty.presentation.characters.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.RecyclerCharacterItemBinding

class CharactersAdapter() : RecyclerView.Adapter<CharactersViewHolder>() {
    private val characterItem = arrayListOf<CharactersUiModel>()

    fun setData(charactersList: List<CharactersUiModel>) {
        characterItem.clear()
        characterItem.addAll(charactersList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder(
            RecyclerCharacterItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = characterItem.size

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.bind(characterItem[position])
    }
}