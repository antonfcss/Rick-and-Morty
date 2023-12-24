package com.example.rickmorty.presentation.episodes

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
import com.example.rickmorty.databinding.EpisodesFragmentBinding
import com.example.rickmorty.presentation.episodes.dialog.EpisodeFilters
import com.example.rickmorty.presentation.episodes.dialog.EpisodesDialogFragment
import com.example.rickmorty.presentation.episodes.recycler.EpisodeItemDecoration
import com.example.rickmorty.presentation.episodes.recycler.EpisodesAdapter
import com.example.rickmorty.presentation.episodes.recycler.EpisodesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodesFragment : BaseFragment<EpisodesFragmentBinding, EpisodesViewModel, EpisodesState>() {
    private val episodesAdapter: EpisodesAdapter by lazy {
        EpisodesAdapter {
            findNavController().navigate(
                R.id.action_episodesFragment_to_aboutEpisodeFragment,
                bundleOf("id_episode" to it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            episodeRecyclerView.adapter = episodesAdapter
            episodeRecyclerView.adapter = episodesAdapter.withLoadStateFooter(
                EpisodesLoadStateAdapter()
            )
            episodeRecyclerView.addItemDecoration(EpisodeItemDecoration(40, 20))
            episodesAdapter.addLoadStateListener { loadState ->
                val refreshState = loadState.refresh
                binding.episodeRecyclerView.isVisible = refreshState != LoadState.Loading
                binding.progress.isVisible = refreshState == LoadState.Loading
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
                    if (episodesAdapter.itemCount < 1) viewModel.onEmptyDataReceiver()
                }
            }
            include.searchButton.setOnClickListener {
                viewModel.getEpisodesListByQuery(include.searchView.query.toString())
            }
            include.filterButton.setOnClickListener {
                viewModel.onFilterButtonClicked()
            }
            viewModel.getFilterLiveData().observe(viewLifecycleOwner) { filter ->
                filter?.let { episodeFilter ->
                    val dialogFragment = EpisodesDialogFragment()
                    val bundle = bundleOf("filter_episodes" to episodeFilter)
                    dialogFragment.arguments = bundle
                    viewModel.postFilterClicked()
                    dialogFragment.show(parentFragmentManager, "tag_episode")
                }
            }
            swipeToRefresh.setOnRefreshListener {
                viewModel.getEpisodesList()
            }
        }
        viewModel.getEpisodesList()
        setFragmentResultListener("filter_episodes") { key, bundle ->
            if (key == "filter_episodes") {
                viewModel.setFilter(
                    bundle.getSerializable("filter_episodes") as EpisodeFilters
                )
            }
        }
    }

    override fun renderSuccessState(viewState: ViewState.Success<EpisodesState>) {
        lifecycleScope.launch {
            episodesAdapter.submitData(viewState.data.episodesList)
        }
        binding.swipeToRefresh.isRefreshing = false
    }
}