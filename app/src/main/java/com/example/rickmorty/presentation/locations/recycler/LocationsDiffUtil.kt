package com.example.rickmorty.presentation.locations.recycler

import androidx.recyclerview.widget.DiffUtil

class LocationsDiffUtil : DiffUtil.ItemCallback<LocationUiModel>() {

    override fun areItemsTheSame(oldItem: LocationUiModel, newItem: LocationUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: LocationUiModel, newItem: LocationUiModel): Boolean {
        return oldItem == newItem
    }
}