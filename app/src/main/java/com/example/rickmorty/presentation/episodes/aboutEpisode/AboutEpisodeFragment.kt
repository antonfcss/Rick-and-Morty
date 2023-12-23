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
import com.example.rickmorty.presentation.episodes.aboutEpisode.recycler.AboutEpisodeItemDecoration
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
        binding.aboutEpisodeRecyclerView.addItemDecoration(AboutEpisodeItemDecoration(40, 20))
        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        arguments?.getInt("id_episode")?.let { viewModel.getAboutEpisodeDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutEpisodeState>) {
        aboutEpisodeAdapter.setData(viewState.data.episodeModelList)
        with(binding) {
            progress.isVisible = false
            charactersTitleTextView.visibility = View.VISIBLE
            backImageView.visibility = View.VISIBLE
            detailsTextView.visibility = View.VISIBLE
            nameEpisodeTextView.text = viewState.data.nameEpisode
            airDataEpisodeTextView.text = viewState.data.airDate
            numberEpisodeTextView.text = viewState.data.dateEpisode
        }
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        with(binding) {
            errorLayout.root.isVisible = true
            errorLayout.retry.setOnClickListener {
                arguments?.getInt("id_episode")?.let { viewModel.getAboutEpisodeDetail(it) }
            }
            errorLayout.textError.text = viewState.message
            progress.isVisible = false
            aboutEpisodeRecyclerView.isVisible = false
            nameEpisodeTextView.isVisible = false
            numberEpisodeTextView.isVisible = false
            airDataEpisodeTextView.isVisible = false
        }
    }
}