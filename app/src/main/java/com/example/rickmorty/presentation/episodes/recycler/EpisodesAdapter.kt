package com.example.rickmorty.presentation.episodes.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.rickmorty.databinding.RecyclerEpisodeItemBinding

class EpisodesAdapter(
    private val onClick: (Int) -> Unit

) : PagingDataAdapter<EpisodesUiModel, EpisodesViewHolder>(EpisodesDiffUtil()) {
    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it, onClick) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        return EpisodesViewHolder(
            RecyclerEpisodeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}