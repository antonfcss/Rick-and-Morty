package com.example.rickmorty.presentation.episodes

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.EpisodesFragmentBinding
import com.example.rickmorty.presentation.episodes.recycler.EpisodesAdapter
import com.example.rickmorty.presentation.episodes.recycler.EpisodesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodesFragment : BaseFragment<EpisodesFragmentBinding, EpisodesViewModel, EpisodesState>() {
    private val episodesAdapter: EpisodesAdapter by lazy { EpisodesAdapter() }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            episodeRecyclerView.adapter = episodesAdapter
            episodeRecyclerView.adapter = episodesAdapter.withLoadStateFooter(
                EpisodesLoadStateAdapter()
            )
            episodesAdapter.addLoadStateListener { state ->
                val refreshState = state.refresh
                episodeRecyclerView.isVisible = refreshState != LoadState.Loading
                progress.isVisible = refreshState == LoadState.Loading
            }
        }
        viewModel.getEpisodesList()
    }

    override fun renderSuccessState(viewState: ViewState.Success<EpisodesState>) {
        lifecycleScope.launch {
            episodesAdapter.submitData(viewState.data.episodesList)
        }
    }
}