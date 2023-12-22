package com.example.rickmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.rickmorty.R
import com.example.rickmorty.base.BaseFragment
import com.example.rickmorty.base.ViewState
import com.example.rickmorty.databinding.CharactersFragmentBinding
import com.example.rickmorty.presentation.characters.dialog.CharacterFilters
import com.example.rickmorty.presentation.characters.dialog.CharactersDialogFragment
import com.example.rickmorty.presentation.characters.recycler.CharactersAdapter
import com.example.rickmorty.presentation.characters.recycler.CharactersItemDecoration
import com.example.rickmorty.presentation.characters.recycler.CharactersLoaderStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<CharactersFragmentBinding, CharactersViewModel, CharactersState>() {

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter {
            findNavController().navigate(
                R.id.action_charactersFragment_to_aboutCharacterFragment,
                bundleOf("id" to it)
            )
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            charactersRecyclerView.adapter = charactersAdapter
            charactersRecyclerView.adapter = charactersAdapter.withLoadStateFooter(
                CharactersLoaderStateAdapter()
            )
            charactersRecyclerView.addItemDecoration(CharactersItemDecoration(2, 2, true, 2))
            charactersAdapter.addLoadStateListener { state ->
                val refreshState = state.refresh
                charactersRecyclerView.isVisible = refreshState != LoadState.Loading
                progress.isVisible = refreshState == LoadState.Loading
            }
            included.searchButton.setOnClickListener {
                viewModel.getCharactersListByQuery(included.searchView.query.toString())
            }
            included.filterButton.setOnClickListener {
                viewModel.onFilterButtonClicked()
            }
            viewModel.getFilterLiveData().observe(viewLifecycleOwner) { filter ->
                filter?.let { characterFilter ->
                    val dialogFragment = CharactersDialogFragment()
                    val bundle = bundleOf("filter" to characterFilter)
                    dialogFragment.arguments = bundle
                    viewModel.postFilterClicked()
                    dialogFragment.show(parentFragmentManager, "tag")
                }
            }
        }
        viewModel.getCharactersList()
        setFragmentResultListener("filter") { key, bundle ->
            if (key == "filter") {
                viewModel.setFilter(
                    bundle.getSerializable("filter") as CharacterFilters
                )
            }
        }

    }

    override fun renderSuccessState(viewState: ViewState.Success<CharactersState>) {
        lifecycleScope.launch {
            charactersAdapter.submitData(viewState.data.charactersList)
        }
    }

}