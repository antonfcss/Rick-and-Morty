package com.example.rickmorty.presentation.aboutLocation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.rickmorty.databinding.LocationCharacterItemBinding

class AboutLocationAdapter(
    private val onCharacterClick: (Int) -> Unit
) : ListAdapter<AboutLocationCharactersModel, AboutLocationCharactersViewHolder>(
    AboutLocationDiffUtil()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AboutLocationCharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AboutLocationCharactersViewHolder(
            LocationCharacterItemBinding.inflate(layoutInflater, parent, false),
            onCharacterClick
        )
    }

    override fun onBindViewHolder(holder: AboutLocationCharactersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}