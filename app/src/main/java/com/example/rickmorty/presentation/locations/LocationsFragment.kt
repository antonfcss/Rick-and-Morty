package com.example.rickmorty.presentation.locations

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.LocationsFragmentBinding
import com.example.rickmorty.presentation.MyItemDecoration
import com.example.rickmorty.presentation.locations.diaolog.LocationFilters
import com.example.rickmorty.presentation.locations.diaolog.LocationsDialogFragment
import com.example.rickmorty.presentation.locations.recycler.LocationLoaderStateAdapter
import com.example.rickmorty.presentation.locations.recycler.LocationsAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment :
    BaseFragment<LocationsFragmentBinding, LocationsViewModel, LocationsState>() {
    companion object {
        private const val FILTER_LOCATION = "filter_location"
        private const val BUNDLE_LOCATION = "id_location"
        private const val TAG_LOCATION = "tag_location"
        private const val VALUE_ONE = 1
        private const val ITEM_DECORATION_SPACING = 40
        private const val ITEM_DECORATION_BOTTOM = 20
    }

    private val locationsAdapter: LocationsAdapter by lazy {
        LocationsAdapter {
            findNavController().navigate(
                R.id.aboutLocationFragment,
                bundleOf(BUNDLE_LOCATION to it)
            )
        }
    }

    private val loadStateListener = { loadState: CombinedLoadStates ->
        val refreshState = loadState.refresh
        binding.locationsRecyclerView.isVisible = refreshState != LoadState.Loading
        binding.progress.isVisible = refreshState == LoadState.Loading
        if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
            if (locationsAdapter.itemCount < VALUE_ONE) viewModel.onEmptyDataReceiver()
        }
        if (loadState.source.refresh is LoadState.Error) {
            FancyToast.makeText(
                requireContext(),
                "Sorry, nothing found. Try again!",
                FancyToast.LENGTH_LONG,
                FancyToast.ERROR,
                true
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            locationsRecyclerView.adapter = locationsAdapter
            locationsRecyclerView.adapter = locationsAdapter.withLoadStateFooter(
                LocationLoaderStateAdapter()
            )
            locationsRecyclerView.addItemDecoration(
                MyItemDecoration(
                    ITEM_DECORATION_SPACING,
                    ITEM_DECORATION_BOTTOM
                )
            )
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
                    val bundle = bundleOf(FILTER_LOCATION to locationFilter)
                    dialogFragment.arguments = bundle
                    viewModel.postFilterClicked()
                    dialogFragment.show(parentFragmentManager, TAG_LOCATION)
                }
            }
        }
        viewModel.getLocationList()
        setFragmentResultListener(FILTER_LOCATION) { key, bundle ->
            if (key == FILTER_LOCATION) {
                viewModel.setFilter(
                    bundle.getSerializable(FILTER_LOCATION) as LocationFilters
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        locationsAdapter.addLoadStateListener(loadStateListener)
    }

    override fun onStop() {
        super.onStop()
        locationsAdapter.removeLoadStateListener(loadStateListener)
    }

    override fun renderSuccessState(viewState: ViewState.Success<LocationsState>) {
        lifecycleScope.launch {
            locationsAdapter.submitData(viewState.data.locationList)
        }
        binding.swipeToRefresh.isRefreshing = false
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        super.renderErrorState(viewState)
        binding.locationsRecyclerView.isVisible = false
    }
}
