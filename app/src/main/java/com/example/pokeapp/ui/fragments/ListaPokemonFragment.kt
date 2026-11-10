package com.example.pokeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentListaPokemonBinding
import com.example.pokeapp.ui.adapter.PokemonAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [ListaPokemonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListaPokemonFragment : Fragment() {
    private var _binding: FragmentListaPokemonBinding? = null
    private val binding get() = _binding!!

    private val adapter = PokemonAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recyclerPokemon.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerPokemon.adapter = adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentListaPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.btnVoltarLista.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}