package com.example.rickmorty.presentation.characters.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.rickmorty.databinding.DialogFilterCharactersFragmentBinding

class CharactersDialogFragment : DialogFragment() {
    private lateinit var binding: DialogFilterCharactersFragmentBinding

    companion object {
        private const val FILTER_CHARACTER = "filter_character"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFilterCharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentFilter = arguments?.getSerializable(FILTER_CHARACTER) as CharacterFilters?
        updateTextColor(currentFilter)
        with(binding) {
            nameFilterTextView.setOnClickListener {
                setFragmentResult(
                    FILTER_CHARACTER,
                    bundleOf(FILTER_CHARACTER to CharacterFilters.NAME)
                )
                dismiss()
            }
            statusFilterTextView.setOnClickListener {
                setFragmentResult(
                    FILTER_CHARACTER,
                    bundleOf(FILTER_CHARACTER to CharacterFilters.STATUS)
                )
                dismiss()
            }
            speciesFilterTextView.setOnClickListener {
                setFragmentResult(
                    FILTER_CHARACTER,
                    bundleOf(FILTER_CHARACTER to CharacterFilters.SPECIES)
                )
                dismiss()
            }
        }


    }

    private fun updateTextColor(currentFilter: CharacterFilters?) {
        when (currentFilter) {
            CharacterFilters.NAME -> {
                binding.nameFilterTextView.setTextColor(Color.GREEN)
            }

            CharacterFilters.STATUS -> {
                binding.statusFilterTextView.setTextColor(Color.GREEN)
            }

            CharacterFilters.SPECIES -> {
                binding.speciesFilterTextView.setTextColor(Color.GREEN)
            }

            else -> {
                binding.nameFilterTextView.setTextColor(Color.BLACK)
                binding.statusFilterTextView.setTextColor(Color.BLACK)
                binding.speciesFilterTextView.setTextColor(Color.BLACK)
            }
        }
    }
}