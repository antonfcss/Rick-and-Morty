package com.example.rickmorty.presentation.aboutEpisode.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.rickmorty.databinding.EpisodeCharacterItemBinding

class AboutEpisodeAdapter(
    private val onCharacterClick: (Int) -> Unit,
) : ListAdapter<AboutEpisodeCharactersModel, AboutEpisodeCharactersViewHolder>(AboutEpisodeDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AboutEpisodeCharactersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AboutEpisodeCharactersViewHolder(
            EpisodeCharacterItemBinding.inflate(layoutInflater, parent, false),
            onCharacterClick
        )
    }
    override fun onBindViewHolder(holder: AboutEpisodeCharactersViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}