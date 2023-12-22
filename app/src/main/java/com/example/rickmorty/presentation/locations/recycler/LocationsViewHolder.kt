package com.example.rickmorty.presentation.locations.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.RecyclerLocationItemBinding

class LocationsViewHolder(private val binding: RecyclerLocationItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(locationUiModel: LocationUiModel, clickListener: (Int) -> Unit) {
        with(binding) {
            locationNameTextView.text = locationUiModel.name
            typeTextView.text = locationUiModel.type
            dimensionTextView.text = locationUiModel.dimension
            root.setOnClickListener { clickListener.invoke(locationUiModel.id) }
        }

    }
}