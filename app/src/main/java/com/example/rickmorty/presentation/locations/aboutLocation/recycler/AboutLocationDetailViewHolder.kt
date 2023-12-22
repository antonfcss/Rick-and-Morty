package com.example.rickmorty.presentation.locations.aboutLocation.recycler

import com.example.rickmorty.databinding.LocationDetailItemBinding
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationDetailModel

class AboutLocationDetailViewHolder(private val binding: LocationDetailItemBinding) :
    AboutLocationViewHolder(binding) {
    fun bind(aboutLocationDetailModel: AboutLocationDetailModel) {
        with(binding) {
            nameLocationTextView.text = aboutLocationDetailModel.name
            locationTypeTextView.text = aboutLocationDetailModel.type
            locationDimensionTextView.text = aboutLocationDetailModel.dimension
        }
    }
}