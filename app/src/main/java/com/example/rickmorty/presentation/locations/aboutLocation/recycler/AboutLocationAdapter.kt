package com.example.rickmorty.presentation.locations.aboutLocation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.LocationCharacterItemBinding

class AboutLocationAdapter(
    private val onCharacterClick: (Int) -> Unit,

    ) : RecyclerView.Adapter<AboutLocationCharactersViewHolder>() {

    private val data = mutableListOf<AboutLocationCharactersModel>()

    fun setData(newData: List<AboutLocationCharactersModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

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

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: AboutLocationCharactersViewHolder, position: Int) {
        holder.bind(data[position])
    }
}