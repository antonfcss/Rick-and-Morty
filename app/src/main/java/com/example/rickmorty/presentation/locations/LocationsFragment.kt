package com.example.rickmorty.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.LocationsFragmentBinding
import com.example.rickmorty.presentation.locations.recycler.LocationLoaderStateAdapter
import com.example.rickmorty.presentation.locations.recycler.LocationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment :
    BaseFragment<LocationsFragmentBinding, LocationsViewModel, LocationsState>() {
    private val locationsAdapter: LocationsAdapter by lazy {
        LocationsAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            locationsRecyclerView.adapter = locationsAdapter
            locationsRecyclerView.adapter = locationsAdapter.withLoadStateFooter(
                LocationLoaderStateAdapter()
            )
            locationsAdapter.addLoadStateListener { state ->
                val refreshState = state.refresh
                locationsRecyclerView.isVisible = refreshState != LoadState.Loading
                progress.isVisible = refreshState == LoadState.Loading
            }
        }
        viewModel.getLocationList()
    }

    override fun renderSuccessState(viewState: ViewState.Success<LocationsState>) {
        lifecycleScope.launch {
            locationsAdapter.submitData(viewState.data.locationList)
        }
    }
}