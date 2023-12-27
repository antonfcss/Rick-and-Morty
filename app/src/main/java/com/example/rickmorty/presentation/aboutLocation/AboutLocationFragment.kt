package com.example.rickmorty.presentation.aboutLocation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutLocatioFragmentBinding
import com.example.rickmorty.presentation.MyItemDecoration
import com.example.rickmorty.presentation.aboutLocation.recycler.AboutLocationAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutLocationFragment :
    BaseFragment<AboutLocatioFragmentBinding, AboutLocationViewModel, AboutLocationState>() {
    companion object {
        private const val BUNDLE_LOCATION = "id_location"
        private const val BUNDLE_CHARACTER = "id_character"
        private const val ITEM_DECORATION_SPACING = 40
        private const val ITEM_DECORATION_BOTTOM = 20
    }

    private val aboutLocationAdapter: AboutLocationAdapter by lazy {
        AboutLocationAdapter {
            findNavController().navigate(
                R.id.aboutCharacterFragment,
                bundleOf(BUNDLE_CHARACTER to it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutLocationRecyclerView.adapter = aboutLocationAdapter
        binding.aboutLocationRecyclerView.addItemDecoration(
            MyItemDecoration(
                ITEM_DECORATION_SPACING,
                ITEM_DECORATION_BOTTOM
            )
        )
        arguments?.getInt(BUNDLE_LOCATION)?.let { viewModel.getDetailAboutLocation(it) }
        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutLocationState>) {
        aboutLocationAdapter.submitList(viewState.data.locationModelList)
        with(binding) {
            progress.isVisible = false
            charactersTitleTextView.visibility = View.VISIBLE
            detailsTextView.visibility = View.VISIBLE
            backImageView.visibility = View.VISIBLE
            nameLocationTextView.text = viewState.data.name
            locationTypeTextView.text = viewState.data.type
            locationDimensionTextView.text = viewState.data.dimension
            errorLayout.root.isVisible = false
            nameLocationTextView.isVisible = true
            locationTypeTextView.isVisible = true
            locationDimensionTextView.isVisible = true
            aboutLocationRecyclerView.isVisible = true
        }
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        with(binding) {
            errorLayout.root.isVisible = true
            errorLayout.retry.setOnClickListener {
                arguments?.getInt(BUNDLE_LOCATION)?.let { viewModel.getDetailAboutLocation(it) }
            }
            errorLayout.textError.text = viewState.message
            progress.isVisible = false
            nameLocationTextView.isVisible = false
            locationTypeTextView.isVisible = false
            locationDimensionTextView.isVisible = false
            aboutLocationRecyclerView.isVisible = false
        }
    }
}
