package com.example.rickmorty.presentation.episodes.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.rickmorty.databinding.DialogFilterEpisodesFragmentBinding

class EpisodesDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFilterEpisodesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFilterEpisodesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentFilter = arguments?.getSerializable("filter_episodes") as EpisodeFilters?
        updateTextColor(currentFilter)
        with(binding) {
            nameFilterTextView.setOnClickListener {
                setFragmentResult(
                    "filter_episodes",
                    bundleOf("filter_episodes" to EpisodeFilters.NAME)
                )
                dismiss()
            }
            episodeFilterTextView.setOnClickListener {
                setFragmentResult(
                    "filter_episodes",
                    bundleOf("filter_episodes" to EpisodeFilters.EPISODE)
                )
                dismiss()
            }
        }
    }

    private fun updateTextColor(currentFilter: EpisodeFilters?) {
        when (currentFilter) {
            EpisodeFilters.NAME -> {
                binding.nameFilterTextView.setTextColor(Color.GREEN)
            }

            EpisodeFilters.EPISODE -> {
                binding.episodeFilterTextView.setTextColor(Color.GREEN)
            }

            else -> {
                binding.nameFilterTextView.setTextColor(Color.BLACK)
                binding.episodeFilterTextView.setTextColor(Color.BLACK)
            }
        }
    }
}