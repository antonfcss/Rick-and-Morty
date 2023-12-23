package com.example.rickmorty.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.LocationsFragmentBinding
import com.example.rickmorty.presentation.locations.diaolog.LocationFilters
import com.example.rickmorty.presentation.locations.diaolog.LocationsDialogFragment
import com.example.rickmorty.presentation.locations.recycler.LocationItemDecoration
import com.example.rickmorty.presentation.locations.recycler.LocationLoaderStateAdapter
import com.example.rickmorty.presentation.locations.recycler.LocationsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment :
    BaseFragment<LocationsFragmentBinding, LocationsViewModel, LocationsState>() {
    private val locationsAdapter: LocationsAdapter by lazy {
        LocationsAdapter {
            findNavController().navigate(
                R.id.action_locationsFragment_to_aboutLocationFragment,
                bundleOf("id_location" to it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            locationsRecyclerView.adapter = locationsAdapter
            locationsRecyclerView.adapter = locationsAdapter.withLoadStateFooter(
                LocationLoaderStateAdapter()
            )
            locationsRecyclerView.addItemDecoration(LocationItemDecoration(40, 20))
            locationsAdapter.addLoadStateListener { loadState ->
                val refreshState = loadState.refresh
                binding.locationsRecyclerView.isVisible = refreshState != LoadState.Loading
                binding.progress.isVisible = refreshState == LoadState.Loading
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                    if (locationsAdapter.itemCount < 1) viewModel.onEmptyDataReceiver()
                }
            }
            include.searchButton.setOnClickListener {
                viewModel.getLocationsListByQuery(include.searchView.query.toString())
            }
            include.filterButton.setOnClickListener {
                viewModel.onFilterButtonClicked()
            }
            swipeToRefresh.setOnRefreshListener {
                viewModel.getLocationList()
            }
            viewModel.getFilterLiveData().observe(viewLifecycleOwner) { filter ->
                filter?.let { locationFilter ->
                    val dialogFragment = LocationsDialogFragment()
                    val bundle = bundleOf("filter_location" to locationFilter)
                    dialogFragment.arguments = bundle
                    viewModel.postFilterClicked()
                    dialogFragment.show(parentFragmentManager, "tag_location")

                }
            }
        }
        viewModel.getLocationList()
        setFragmentResultListener("filter_location") { key, bundle ->
            if (key == "filter_location") {
                viewModel.setFilter(
                    bundle.getSerializable("filter_location") as LocationFilters
                )
            }
        }
    }

    override fun renderSuccessState(viewState: ViewState.Success<LocationsState>) {
        lifecycleScope.launch {
            locationsAdapter.submitData(viewState.data.locationList)
        }
        binding.swipeToRefresh.isRefreshing = false
    }
}
