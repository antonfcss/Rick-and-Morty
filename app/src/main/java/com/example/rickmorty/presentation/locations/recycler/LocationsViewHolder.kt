package com.example.rickmorty.presentation.locations.recycler

import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.RecyclerLocationItemBinding

class LocationsViewHolder(private val binding: RecyclerLocationItemBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(locationUiModel: LocationUiModel) {
        binding.locationNameTextView.text = locationUiModel.name
        binding.typeTextView.text = locationUiModel.type
        binding.dimensionTextView.text = locationUiModel.dimension
    }
}