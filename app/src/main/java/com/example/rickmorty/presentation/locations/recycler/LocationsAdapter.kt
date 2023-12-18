package com.example.rickmorty.presentation.locations.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.rickmorty.databinding.RecyclerLocationItemBinding

class LocationsAdapter :
    PagingDataAdapter<LocationUiModel, LocationsViewHolder>(LocationsDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        return LocationsViewHolder(
            RecyclerLocationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }


}