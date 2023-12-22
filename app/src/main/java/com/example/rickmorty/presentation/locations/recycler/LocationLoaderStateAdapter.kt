package com.example.rickmorty.presentation.locations.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickmorty.databinding.ItemErrorBinding
import com.example.rickmorty.databinding.ItemProgressBinding

class LocationLoaderStateAdapter(
) : LoadStateAdapter<LocationLoaderStateAdapter.ItemViewHolder>() {

    override fun getStateViewType(loadState: LoadState) = when (loadState) {
        is LoadState.NotLoading -> ERROR
        LoadState.Loading -> PROGRESS
        is LoadState.Error -> ERROR
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ItemViewHolder {
        return when (loadState) {
            LoadState.Loading -> ProgressViewHolder(parent)
            is LoadState.Error -> ErrorViewHolder(parent)
            is LoadState.NotLoading -> ErrorViewHolder(parent)
        }
    }

    private companion object {

        private const val ERROR = 1
        private const val PROGRESS = 0

    }

    override fun onBindViewHolder(holder: ItemViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        abstract fun bind(loadState: LoadState)
    }

    class ProgressViewHolder internal constructor(
        binding: ItemProgressBinding
    ) : ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {
        }

        companion object {

            operator fun invoke(parent: ViewGroup): ProgressViewHolder {
                return ProgressViewHolder(
                    ItemProgressBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    class ErrorViewHolder internal constructor(
        private val binding: ItemErrorBinding
    ) : ItemViewHolder(binding.root) {

        override fun bind(loadState: LoadState) {
            require(loadState is LoadState.Error)
            binding.errorMessage.text = loadState.error.localizedMessage
        }

        companion object {

            operator fun invoke(parent: ViewGroup): ErrorViewHolder {
                return ErrorViewHolder(
                    ItemErrorBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }

        }
    }
}