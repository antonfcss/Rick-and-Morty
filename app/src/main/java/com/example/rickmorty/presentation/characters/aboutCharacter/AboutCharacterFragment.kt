package com.example.rickmorty.presentation.characters.aboutCharacter

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.AboutCharacterFragmentBinding
import com.example.rickmorty.presentation.characters.aboutCharacter.recycler.AboutCharacterAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AboutCharacterFragment :
    BaseFragment<AboutCharacterFragmentBinding, AboutCharacterViewModel, AboutCharacterState>() {

    private val aboutCharacterAdapter: AboutCharacterAdapter by lazy {
        AboutCharacterAdapter(
            onLocationClicked = {
//                findNavController().navigate(R.id, bundleOf("id" to it))
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutCharactersRecyclerView.adapter = aboutCharacterAdapter
        arguments?.getInt("id")?.let { viewModel.getAboutCharacterDetail(it) }
    }

    override fun renderSuccessState(viewState: ViewState.Success<AboutCharacterState>) {
        lifecycleScope.launch {
            aboutCharacterAdapter.submitList(viewState.data.characterModelsList)
        }
    }
}