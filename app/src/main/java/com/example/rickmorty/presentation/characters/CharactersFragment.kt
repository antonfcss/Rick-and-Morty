package com.example.rickmorty.presentation.characters

import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.CharactersFragmentBinding

class CharactersFragment :
    BaseFragment<CharactersFragmentBinding, CharactersViewModel, CharactersState>() {
    override fun renderSuccessState(viewState: ViewState.Success<CharactersState>) {
        TODO("Not yet implemented")
    }

    override fun renderErrorState(viewState: ViewState.Error) {
        TODO("Not yet implemented")
    }

    override fun renderLoadingState(viewState: ViewState.Loading) {
        TODO("Not yet implemented")
    }
}