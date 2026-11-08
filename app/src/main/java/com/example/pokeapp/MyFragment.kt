package com.example.pokeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.pokeapp.databinding.FragmentABinding

class MyFragment : Fragment(R.layout.fragment_a) {

    private var _binding: FragmentABinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentABinding.inflate(inflater, container, false)
        binding.textFragmentA.setText(R.string.txt_fragment_a_name)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
