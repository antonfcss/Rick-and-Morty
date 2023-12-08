package com.example.rickmorty.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.rickmorty.databinding.ContainerFragmentBinding

class ContainerFragment : Fragment() {

    private lateinit var binding: ContainerFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ContainerFragmentBinding.bind(view)
    }

}