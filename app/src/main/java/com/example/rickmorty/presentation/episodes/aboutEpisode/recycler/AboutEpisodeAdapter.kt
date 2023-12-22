package com.example.rickmorty.presentation.episodes.aboutEpisode.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.EpisodeCharacterItemBinding
import com.example.rickmorty.databinding.EpisodeDetailItemBinding
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeCharactersModel
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeDetailModel
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.model.AboutEpisodeRecyclerModel

class AboutEpisodeAdapter() : RecyclerView.Adapter<AboutEpisodeViewHolder>() {

    private val data = mutableListOf<AboutEpisodeRecyclerModel>()

    fun setData(newData: List<AboutEpisodeRecyclerModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private companion object {
        const val ABOUT_EPISODE_DETAIL = 0
        const val ABOUT_EPISODE_CHARACTER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ABOUT_EPISODE_DETAIL
        } else {
            ABOUT_EPISODE_CHARACTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutEpisodeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ABOUT_EPISODE_DETAIL -> AboutEpisodeDetailViewHolder(
                EpisodeDetailItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            ABOUT_EPISODE_CHARACTER -> AboutEpisodeCharactersViewHolder(
                EpisodeCharacterItemBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw IllegalArgumentException("Ошибка ViewType")
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AboutEpisodeViewHolder, position: Int) {
        when (holder) {
            is AboutEpisodeDetailViewHolder -> holder.bind(data[position] as AboutEpisodeDetailModel)
            is AboutEpisodeCharactersViewHolder -> holder.bind(data[position] as AboutEpisodeCharactersModel)
        }
    }
}