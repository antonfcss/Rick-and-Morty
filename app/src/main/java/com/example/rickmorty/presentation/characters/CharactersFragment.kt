package com.example.rickmorty.presentation.characters

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.CharactersFragmentBinding
import com.example.rickmorty.presentation.characters.recycler.CharactersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<CharactersFragmentBinding, CharactersViewModel, CharactersState>() {

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CharactersFragment", "onViewCreated: CharactersFragment created!")
        binding.charactersRecyclerView.adapter = charactersAdapter
        viewModel.getCharactersList()

    }

    override fun renderSuccessState(viewState: ViewState.Success<CharactersState>) {
        charactersAdapter.setData(viewState.data.charactersList)
    }

}