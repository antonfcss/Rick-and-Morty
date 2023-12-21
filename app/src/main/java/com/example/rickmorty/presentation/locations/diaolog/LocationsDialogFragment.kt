package com.example.rickmorty.presentation.locations.diaolog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.rickmorty.databinding.DialogFilterLocationsFragmentBinding

class LocationsDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFilterLocationsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFilterLocationsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentFilter = arguments?.getSerializable("filter_location") as LocationFilters?
        updateTextColor(currentFilter)
        with(binding) {
            nameFilterTextView.setOnClickListener {
                setFragmentResult(
                    "filter_location",
                    bundleOf("filter_location" to LocationFilters.NAME)
                )
                dismiss()
            }
            typeFilterTextView.setOnClickListener {
                setFragmentResult(
                    "filter_location",
                    bundleOf("filter_location" to LocationFilters.TYPE)
                )
                dismiss()
            }
            dimensionFilterTextView.setOnClickListener {
                setFragmentResult(
                    "filter_location",
                    bundleOf("filter_location" to LocationFilters.DIMENSION)
                )
                dismiss()
            }
        }
    }

    private fun updateTextColor(currentFilter: LocationFilters?) {
        when (currentFilter) {
            LocationFilters.NAME -> {
                binding.nameFilterTextView.setTextColor(Color.GREEN)
            }

            LocationFilters.TYPE -> {
                binding.typeFilterTextView.setTextColor(Color.GREEN)
            }

            LocationFilters.DIMENSION -> {
                binding.dimensionFilterTextView.setTextColor(Color.GREEN)
            }

            else -> {
                binding.nameFilterTextView.setTextColor(Color.BLACK)
                binding.typeFilterTextView.setTextColor(Color.BLACK)
                binding.dimensionFilterTextView.setTextColor(Color.BLACK)
            }
        }
    }
}