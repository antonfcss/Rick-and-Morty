package com.example.rickmorty.presentation.episodes

import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.EpisodesFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EpisodesFragment : BaseFragment<EpisodesFragmentBinding, EpisodesViewModel, EpisodesState>() {
    override fun renderSuccessState(viewState: ViewState.Success<EpisodesState>) {
        TODO("Not yet implemented")
    }
}