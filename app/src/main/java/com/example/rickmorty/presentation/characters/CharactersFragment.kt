package com.example.rickmorty.presentation.characters

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
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
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment :
    BaseFragment<CharactersFragmentBinding, CharactersViewModel, CharactersState>() {

    companion object {
        private const val FILTER_CHARACTER = "filter_character"
        private const val VALUE_ONE = 1
        private const val TAG_CHARACTER = "tag_character"
        private const val BUNDLE_CHARACTER = "id_character"
    }

    private val charactersAdapter: CharactersAdapter by lazy {
        CharactersAdapter {
            findNavController().navigate(
                R.id.aboutCharacterFragment,
                bundleOf(BUNDLE_CHARACTER to it)
            )
        }
    }
    private val loadStateListener = { loadState: CombinedLoadStates ->
        val refreshState = loadState.refresh
        binding.charactersRecyclerView.isVisible = refreshState != LoadState.Loading
        binding.progress.isVisible = refreshState == LoadState.Loading
        if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached) {
            if (charactersAdapter.itemCount < VALUE_ONE) viewModel.onEmptyDataReceiver()
        }
        if (loadState.source.refresh is LoadState.Error) {
            FancyToast.makeText(
                requireContext(),
                "Sorry, nothing found. Try again!",
                FancyToast.LENGTH_LONG,
                FancyToast.ERROR,
                true
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            charactersRecyclerView.adapter = charactersAdapter
            charactersRecyclerView.adapter = charactersAdapter.withLoadStateFooter(
                CharactersLoaderStateAdapter()
            )
            charactersRecyclerView.addItemDecoration(CharactersItemDecoration(40, 20))
            included.searchButton.setOnClickListener {
                viewModel.getCharactersListByQuery(included.searchView.query.toString())
            }
            included.filterButton.setOnClickListener {
                viewModel.onFilterButtonClicked()
            }
            swipeToRefresh.setOnRefreshListener {
                viewModel.getCharactersList()
            }
            viewModel.getFilterLiveData().observe(viewLifecycleOwner) { filter ->
                filter?.let { characterFilter ->
                    val dialogFragment = CharactersDialogFragment()
                    val bundle = bundleOf(FILTER_CHARACTER to characterFilter)
                    dialogFragment.arguments = bundle
                    viewModel.postFilterClicked()
                    dialogFragment.show(parentFragmentManager, TAG_CHARACTER)
                }
            }
        }
        viewModel.getCharactersList()
        setFragmentResultListener(FILTER_CHARACTER) { key, bundle ->
            if (key == FILTER_CHARACTER) {
                viewModel.setFilter(
                    bundle.getSerializable(FILTER_CHARACTER) as CharacterFilters
                )
            }
        }

    }

    override fun onResume() {
        super.onResume()
        charactersAdapter.addLoadStateListener(loadStateListener)
    }

    override fun onStop() {
        super.onStop()
        charactersAdapter.removeLoadStateListener(loadStateListener)
    }

    override fun renderSuccessState(viewState: ViewState.Success<CharactersState>) {
        lifecycleScope.launch {
            charactersAdapter.submitData(viewState.data.charactersList)
        }
        binding.swipeToRefresh.isRefreshing = false

    }

}