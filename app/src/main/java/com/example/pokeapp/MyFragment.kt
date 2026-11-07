package com.example.pokeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class MyFragment : Fragment() {
    private lateinit var binding: InputBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        val fragView = inflater.inflate(R.layout.fragment_a, container, false)
        return fragView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textFragmentA.setOnClickListener({
            findNavController().navigate(R.id.action_myFragment_to_blankFragment)
        })

    }


}