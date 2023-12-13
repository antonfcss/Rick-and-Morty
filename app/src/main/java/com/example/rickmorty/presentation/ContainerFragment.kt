package com.example.rickmorty.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rickmorty.databinding.ContainerFragmentBinding

class ContainerFragment : Fragment() {

    private lateinit var binding: ContainerFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContainerFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}