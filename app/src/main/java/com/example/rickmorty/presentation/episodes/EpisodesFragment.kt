package com.example.rickmorty.presentation.episodes

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
import com.example.rickmorty.databinding.EpisodesFragmentBinding
import com.example.rickmorty.presentation.MyItemDecoration
import com.example.rickmorty.presentation.episodes.dialog.EpisodeFilters
import com.example.rickmorty.presentation.episodes.dialog.EpisodesDialogFragment
import com.example.rickmorty.presentation.episodes.recycler.EpisodesAdapter
import com.example.rickmorty.presentation.episodes.recycler.EpisodesLoadStateAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodesFragment : BaseFragment<EpisodesFragmentBinding, EpisodesViewModel, EpisodesState>() {
    companion object {
        private const val FILTER_EPISODE = "filter_episodes"
        private const val VALUE_ONE = 1
        private const val BUNDLE_EPISODE = "id_episode"
        private const val TAG_EPISODE = "tag_episode"
        private const val ITEM_DECORATION_SPACING = 40
        private const val ITEM_DECORATION_BOTTOM = 20
    }

    private val episodesAdapter: EpisodesAdapter by lazy {
        EpisodesAdapter {
            findNavController().navigate(
                R.id.aboutEpisodeFragment,
                bundleOf(BUNDLE_EPISODE to it)
            )
        }
    }
    private val loadStateListener = { loadState: CombinedLoadStates ->
        val refreshState = loadState.refresh
        binding.episodeRecyclerView.isVisible = refreshState != LoadState.Loading
        binding.progress.isVisible = refreshState == LoadState.Loading
        if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
            if (episodesAdapter.itemCount < VALUE_ONE) viewModel.onEmptyDataReceiver()
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
            episodeRecyclerView.adapter = episodesAdapter
            episodeRecyclerView.adapter = episodesAdapter.withLoadStateFooter(
                EpisodesLoadStateAdapter()
            )
            episodeRecyclerView.addItemDecoration(
                MyItemDecoration(
                    ITEM_DECORATION_SPACING,
                    ITEM_DECORATION_BOTTOM
                )
            )
            include.searchButton.setOnClickListener {
                viewModel.getEpisodesListByQuery(include.searchView.query.toString())
            }
            include.filterButton.setOnClickListener {
                viewModel.onFilterButtonClicked()
            }
            viewModel.getFilterLiveData().observe(viewLifecycleOwner) { filter ->
                filter?.let { episodeFilter ->
                    val dialogFragment = EpisodesDialogFragment()
                    val bundle = bundleOf(FILTER_EPISODE to episodeFilter)
                    dialogFragment.arguments = bundle
                    viewModel.postFilterClicked()
                    dialogFragment.show(parentFragmentManager, TAG_EPISODE)
                }
            }
            swipeToRefresh.setOnRefreshListener {
                viewModel.getEpisodesList()
            }
        }
        viewModel.getEpisodesList()
        setFragmentResultListener(FILTER_EPISODE) { key, bundle ->
            if (key == FILTER_EPISODE) {
                viewModel.setFilter(
                    bundle.getSerializable(FILTER_EPISODE) as EpisodeFilters
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        episodesAdapter.addLoadStateListener(loadStateListener)
    }

    override fun onStop() {
        super.onStop()
        episodesAdapter.removeLoadStateListener(loadStateListener)

    }

    override fun renderSuccessState(viewState: ViewState.Success<EpisodesState>) {
        lifecycleScope.launch {
            episodesAdapter.submitData(viewState.data.episodesList)
        }
        binding.swipeToRefresh.isRefreshing = false
    }
}