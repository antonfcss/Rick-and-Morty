package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.EpisodeCharacterItemBinding

class AboutEpisodeAdapter(
    private val onCharacterClick: (Int) -> Unit,
) : RecyclerView.Adapter<AboutEpisodeCharactersViewHolder>() {

    private val data = mutableListOf<AboutEpisodeCharactersModel>()

    fun setData(newData: List<AboutEpisodeCharactersModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }


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

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AboutEpisodeCharactersViewHolder, position: Int) {
        holder.bind(data[position])
    }
}