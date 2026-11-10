package com.example.pokeapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentBuscaBinding
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [BuscaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BuscaFragment : Fragment() {

    private var _binding: FragmentBuscaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentBuscaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navega para a lista de Pokémon.
        binding.btnAbrirLista.setOnClickListener {
            findNavController().navigate(
                R.id.action_buscaFragment_to_listaPokemonFragment
            )
        }

        // Navega para os favoritos.
        binding.btnAbrirFavoritos.setOnClickListener {
            findNavController().navigate(
                R.id.action_buscaFragment_to_favoritosFragment
            )
        }

        // Abre os detalhes de um Pokémon informado pelo usuário.
        binding.btnVerDetalhes.setOnClickListener {

            val pokemon = binding.editPokemon.text
                .toString()
                .trim()
                .lowercase(Locale.ROOT)

            if (pokemon.isBlank()) {
                binding.editPokemon.error = getString(R.string.erro_nome_vazio)
                return@setOnClickListener
            }

            val action =
                BuscaFragmentDirections.actionBuscaFragmentToDetalhesPokemonFragment(pokemon)

            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}