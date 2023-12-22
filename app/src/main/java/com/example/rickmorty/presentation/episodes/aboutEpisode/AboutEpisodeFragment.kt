package com.example.rickmorty.presentation.episodes.aboutEpisode

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutEpisodeFragmentBinding
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.AboutEpisodeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutEpisodeFragment :
    BaseFragment<AboutEpisodeFragmentBinding, AboutEpisodeViewModel, AboutEpisodeState>() {

    private val aboutEpisodeAdapter: AboutEpisodeAdapter by lazy {
        AboutEpisodeAdapter {
            findNavController().navigate(
                R.id.action_aboutEpisodeFragment_to_aboutCharacterFragment,
                bundleOf("id" to it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutEpisodeRecyclerView.adapter = aboutEpisodeAdapter
        arguments?.getInt("id_episode")?.let { viewModel.getAboutEpisodeDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutEpisodeState>) {
        aboutEpisodeAdapter.setData(viewState.data.episodeModelList)
        with(binding) {
            progress.isVisible = false
            nameEpisodeTextView.text = viewState.data.nameEpisode
            airDataEpisodeTextView.text = viewState.data.airDate
            numberEpisodeTextView.text = viewState.data.dateEpisode
        }


    }
}