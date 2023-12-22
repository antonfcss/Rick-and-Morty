package com.example.rickmorty.presentation.locations.aboutLocation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutLocatioFragmentBinding
import com.example.rickmorty.presentation.locations.aboutLocation.recycler.AboutLocationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AboutLocationFragment :
    BaseFragment<AboutLocatioFragmentBinding, AboutLocationViewModel, AboutLocationState>() {

    private val aboutLocationAdapter: AboutLocationAdapter by lazy {
        AboutLocationAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutLocationRecyclerView.adapter = aboutLocationAdapter
        arguments?.getInt("id_location")?.let { viewModel.getDetailAboutLocation(it) }

    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutLocationState>) {
        lifecycleScope.launch {
            aboutLocationAdapter.setData(viewState.data.locationModelList)
        }
    }
}
