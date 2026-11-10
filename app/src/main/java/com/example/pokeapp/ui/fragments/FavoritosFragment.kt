package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentFavoritosBinding
import com.example.pokeapp.repository.api.model.PokemonListItem
import com.example.pokeapp.ui.adapter.PokemonAdapter
import com.example.pokeapp.ui.viewModel.FavoritosViewModel

class FavoritosFragment : Fragment() {

    private var _binding: FragmentFavoritosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PokemonAdapter { pokemon ->
            val args = Bundle().apply {
                putString("pokemonKey", pokemon.name)
            }
            findNavController().navigate(R.id.detalhesPokemonFragment, args)
        }

        binding.recyclerFavoritos.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerFavoritos.adapter = adapter

        viewModel.favoritos.observe(viewLifecycleOwner) { favoritos ->
            val itens = favoritos.map { favorito ->
                PokemonListItem(
                    name = favorito.name,
                    url = "https://pokeapi.co/api/v2/pokemon/${favorito.id}/"
                )
            }
            adapter.atualizarItens(itens)
            binding.txtFavoritosVazio.visibility =
                if (favoritos.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.btnVoltarFavoritos.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        binding.recyclerFavoritos.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
