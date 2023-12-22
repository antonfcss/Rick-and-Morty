package com.example.rickmorty.presentation.characters.aboutCharacter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutCharacterFragmentBinding
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.AboutCharacterAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutCharacterFragment :
    BaseFragment<AboutCharacterFragmentBinding, AboutCharacterViewModel, AboutCharacterState>() {

    private val aboutCharacterAdapter: AboutCharacterAdapter by lazy {
        AboutCharacterAdapter(onEpisodeClicked = {
            findNavController().navigate(
                R.id.action_aboutCharacterFragment_to_aboutEpisodeFragment,
                bundleOf("id_episode" to it)
            )
        },

            onLocationClicked = {
                findNavController().navigate(
                    R.id.action_aboutCharacterFragment_to_aboutLocationFragment,
                    bundleOf("id_location" to it)
                )
            })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutCharactersRecyclerView.adapter = aboutCharacterAdapter
        arguments?.getInt("id")?.let { viewModel.getAboutCharacterDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutCharacterState>) {
        aboutCharacterAdapter.setData(viewState.data.characterModelsList)
        binding.progress.isVisible = false
    }
}