package com.example.rickmorty.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rickmorty.R
import com.example.rickmorty.databinding.ContainerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainerFragment : Fragment() {
    private lateinit var binding: ContainerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContainerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navContainer =
            childFragmentManager.findFragmentById(R.id.navGraphContainer)?.findNavController()
        navContainer?.let { binding.bottomNavigation.setupWithNavController(it) }
        binding.bottomNavigation.selectedItemId = R.id.charactersFragment

    }

}