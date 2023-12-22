package com.example.rickmorty.presentation.locations.aboutLocation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutLocatioFragmentBinding
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.AboutLocationAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AboutLocationFragment :
    BaseFragment<AboutLocatioFragmentBinding, AboutLocationViewModel, AboutLocationState>() {

    private val aboutLocationAdapter: AboutLocationAdapter by lazy {
        AboutLocationAdapter {
            findNavController().navigate(
                R.id.action_aboutLocationFragment_to_aboutCharacterFragment,
                bundleOf("id" to it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutLocationRecyclerView.adapter = aboutLocationAdapter
        arguments?.getInt("id_location")?.let { viewModel.getDetailAboutLocation(it) }

    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutLocationState>) {
        binding.progress.isVisible = false
        aboutLocationAdapter.setData(viewState.data.locationModelList)
        binding.nameLocationTextView.text = viewState.data.name
        binding.locationTypeTextView.text = viewState.data.type
        binding.locationDimensionTextView.text = viewState.data.dimension

    }
}
