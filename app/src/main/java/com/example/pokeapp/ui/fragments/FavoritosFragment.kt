package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentFavoritosBinding
import com.example.pokeapp.ui.adapter.FavoritosAdapter
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

        val adapter = FavoritosAdapter(
            onAbrir = { favorito ->
                val args = Bundle().apply { putString("pokemonKey", favorito.name) }
                findNavController().navigate(R.id.detalhesPokemonFragment, args)
            },
            onRemover = { favorito -> viewModel.removerFavorito(favorito.id) }
        )

        binding.recyclerFavoritos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerFavoritos.adapter = adapter

        viewModel.favoritos.observe(viewLifecycleOwner) { favoritos ->
            adapter.submitList(favoritos)
            binding.layoutFavoritosVazio.isVisible = favoritos.isEmpty()
        }

        viewModel.erro.observe(viewLifecycleOwner) { mensagem ->
            if (mensagem != null) {
                Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
                viewModel.limparErro()
            }
        }
    }

    override fun onDestroyView() {
        binding.recyclerFavoritos.adapter = null
        _binding = null
        super.onDestroyView()
    }
}
