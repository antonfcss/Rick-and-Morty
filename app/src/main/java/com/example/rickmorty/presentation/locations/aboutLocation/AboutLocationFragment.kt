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
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.AboutLocationItemDecoration
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
        binding.aboutLocationRecyclerView.addItemDecoration(AboutLocationItemDecoration(40, 20))
        arguments?.getInt("id_location")?.let { viewModel.getDetailAboutLocation(it) }
        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutLocationState>) {
        aboutLocationAdapter.setData(viewState.data.locationModelList)
        with(binding) {
            progress.isVisible = false
            charactersTitleTextView.visibility = View.VISIBLE
            detailsTextView.visibility = View.VISIBLE
            backImageView.visibility = View.VISIBLE
            nameLocationTextView.text = viewState.data.name
            locationTypeTextView.text = viewState.data.type
            locationDimensionTextView.text = viewState.data.dimension
        }
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        with(binding) {
            errorLayout.root.isVisible = true
            errorLayout.retry.setOnClickListener {
                arguments?.getInt("id_location")?.let { viewModel.getDetailAboutLocation(it) }
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
