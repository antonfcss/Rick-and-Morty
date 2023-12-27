package com.example.rickmorty.presentation.aboutEpisode

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutEpisodeFragmentBinding
import com.example.rickmorty.presentation.MyItemDecoration
import com.example.rickmorty.presentation.aboutEpisode.recycler.AboutEpisodeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutEpisodeFragment :
    BaseFragment<AboutEpisodeFragmentBinding, AboutEpisodeViewModel, AboutEpisodeState>() {

    companion object {
        private const val BUNDLE_CHARACTER = "id_character"
        private const val BUNDLE_EPISODE = "id_episode"
        private const val ITEM_DECORATION_SPACING = 40
        private const val ITEM_DECORATION_BOTTOM = 20
    }

    private val aboutEpisodeAdapter: AboutEpisodeAdapter by lazy {
        AboutEpisodeAdapter {
            findNavController().navigate(
                R.id.aboutCharacterFragment,
                bundleOf(BUNDLE_CHARACTER to it)
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutEpisodeRecyclerView.adapter = aboutEpisodeAdapter
        binding.aboutEpisodeRecyclerView.addItemDecoration(
            MyItemDecoration(
                ITEM_DECORATION_SPACING,
                ITEM_DECORATION_BOTTOM
            )
        )
        binding.backImageView.setOnClickListener {
            findNavController().popBackStack()
        }
        arguments?.getInt(BUNDLE_EPISODE)?.let { viewModel.getAboutEpisodeDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutEpisodeState>) {
        aboutEpisodeAdapter.submitList(viewState.data.episodeModelList)
        with(binding) {
            progress.isVisible = false
            charactersTitleTextView.visibility = View.VISIBLE
            backImageView.visibility = View.VISIBLE
            detailsTextView.visibility = View.VISIBLE
            nameEpisodeTextView.text = viewState.data.nameEpisode
            airDataEpisodeTextView.text = viewState.data.airDate
            numberEpisodeTextView.text = viewState.data.dateEpisode
            aboutEpisodeRecyclerView.isVisible = true
            nameEpisodeTextView.isVisible = true
            numberEpisodeTextView.isVisible = true
            airDataEpisodeTextView.isVisible = true
        }
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        with(binding) {
            errorLayout.root.isVisible = true
            errorLayout.retry.setOnClickListener {
                arguments?.getInt(BUNDLE_EPISODE)?.let { viewModel.getAboutEpisodeDetail(it) }
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