package com.example.rickmorty.presentation.aboutCharacter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutCharacterFragmentBinding
import com.example.rickmorty.presentation.aboutCharacter.recycler.AboutCharacterAdapter
import com.example.rickmorty.presentation.aboutCharacter.recycler.AboutCharacterItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutCharacterFragment :
    BaseFragment<AboutCharacterFragmentBinding, AboutCharacterViewModel, AboutCharacterState>() {

    companion object {
        private const val BUNDLE_CHARACTER = "id_character"
        private const val BUNDLE_LOCATION = "id_location"
        private const val BUNDLE_EPISODE = "id_episode"
        private const val ITEM_DECORATION_SPACING = 50
    }

    private val aboutCharacterAdapter: AboutCharacterAdapter by lazy {
        AboutCharacterAdapter(
            onEpisodeClicked = {
                findNavController().navigate(
                    R.id.aboutEpisodeFragment,
                    bundleOf(BUNDLE_EPISODE to it)
                )
            },
            onLocationClicked = {
                findNavController().navigate(
                    R.id.aboutLocationFragment,
                    bundleOf(BUNDLE_LOCATION to it)
                )
            },
            onOriginClicked = {
                findNavController().navigate(
                    R.id.aboutLocationFragment,
                    bundleOf(BUNDLE_LOCATION to it)
                )
            },
            onBackClicked = {
                findNavController().popBackStack()
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutCharactersRecyclerView.adapter = aboutCharacterAdapter
        binding.aboutCharactersRecyclerView.addItemDecoration(
            AboutCharacterItemDecoration(
                ITEM_DECORATION_SPACING
            )
        )
        arguments?.getInt(BUNDLE_CHARACTER)?.let { viewModel.getAboutCharacterDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutCharacterState>) {
        aboutCharacterAdapter.submitList(viewState.data.characterModelsList)
        with(binding) {
            progress.isVisible = false
            aboutCharactersRecyclerView.isVisible = true
            errorLayout.root.isVisible = false

        }
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        with(binding) {
            errorLayout.root.isVisible = true
            errorLayout.retry.setOnClickListener {
                arguments?.getInt(BUNDLE_CHARACTER)?.let { viewModel.getAboutCharacterDetail(it) }
            }
            errorLayout.textError.text = viewState.message
            progress.isVisible = false
            aboutCharactersRecyclerView.isVisible = false
        }
    }
}