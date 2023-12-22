package com.example.rickmorty.presentation.locations.aboutLocation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.LocationCharacterItemBinding
import com.example.rickmorty.databinding.LocationDetailItemBinding
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationCharactersModel
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationDetailModel
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.model.AboutLocationRecyclerModel

class AboutLocationAdapter() : RecyclerView.Adapter<AboutLocationViewHolder>() {

    private val data = mutableListOf<AboutLocationRecyclerModel>()

    fun setData(newData: List<AboutLocationRecyclerModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    private companion object {
        const val ABOUT_LOCATION_DETAIL = 0
        const val ABOUT_LOCATION_CHARACTER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ABOUT_LOCATION_DETAIL
        } else {
            ABOUT_LOCATION_CHARACTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutLocationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ABOUT_LOCATION_DETAIL -> AboutLocationDetailViewHolder(
                LocationDetailItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            ABOUT_LOCATION_CHARACTER -> AboutLocationCharactersViewHolder(
                LocationCharacterItemBinding.inflate(layoutInflater, parent, false)
            )

            else -> throw IllegalArgumentException("Ошибка ViewType")
        }

    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: AboutLocationViewHolder, position: Int) {
        when (holder) {
            is AboutLocationDetailViewHolder -> holder.bind(data[position] as AboutLocationDetailModel)
            is AboutLocationCharactersViewHolder -> holder.bind(data[position] as AboutLocationCharactersModel)
        }
    }
}