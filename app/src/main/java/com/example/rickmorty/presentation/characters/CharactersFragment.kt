package com.example.rickmorty.presentation.characters

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.CharactersFragmentBinding
import com.example.rickmorty.presentation.characters.recycler.CharactersAdapter
import com.example.rickmorty.presentation.characters.recycler.CharactersLoaderStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<CharactersFragmentBinding, CharactersViewModel, CharactersState>() {

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CharactersFragment", "onViewCreated: CharactersFragment created!")
        with(binding) {
            charactersRecyclerView.adapter = charactersAdapter
            charactersRecyclerView.adapter = charactersAdapter.withLoadStateFooter(
                CharactersLoaderStateAdapter()
            )
            charactersAdapter.addLoadStateListener { state ->
                val refreshState = state.refresh
                charactersRecyclerView.isVisible = refreshState != LoadState.Loading
                progress.isVisible = refreshState == LoadState.Loading
            }
        }
        viewModel.getCharactersList()

    }

    override fun renderSuccessState(viewState: ViewState.Success<CharactersState>) {
        lifecycleScope.launch {
            charactersAdapter.submitData(viewState.data.charactersList)
        }
    }

}