package com.example.rickmorty.presentation.episodes.aboutEpisode

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutEpisodeFragmentBinding
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.AboutEpisodeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AboutEpisodeFragment :
    BaseFragment<AboutEpisodeFragmentBinding, AboutEpisodeViewModel, AboutEpisodeState>() {

    private val aboutEpisodeAdapter: AboutEpisodeAdapter by lazy {
        AboutEpisodeAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutEpisodeRecyclerView.adapter = aboutEpisodeAdapter
        arguments?.getInt("id_episode")?.let { viewModel.getAboutEpisodeDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutEpisodeState>) {
        lifecycleScope.launch {
            aboutEpisodeAdapter.setData(viewState.data.episodeModelList)
        }
    }
}