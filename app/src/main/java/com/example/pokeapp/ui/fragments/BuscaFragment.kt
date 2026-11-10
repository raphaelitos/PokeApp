package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentBuscaBinding
import java.util.Locale

class BuscaFragment : Fragment() {

    private var _binding: FragmentBuscaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBuscar.setOnClickListener { buscar(binding.editPokemon.text.toString()) }

        // Tecla de busca do teclado.
        binding.editPokemon.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                buscar(binding.editPokemon.text.toString())
                true
            } else {
                false
            }
        }

        binding.editPokemon.doAfterTextChanged { binding.layoutPokemon.error = null }

        // Sugestões rápidas.
        listOf(binding.chipPikachu, binding.chipCharmander, binding.chipBulbasaur)
            .forEach { chip -> chip.setOnClickListener { buscar(chip.text.toString()) } }
    }

    // Abre os detalhes do Pokémon informado pelo usuário.
    private fun buscar(texto: String) {
        val pokemon = texto.trim().lowercase(Locale.ROOT)

        if (pokemon.isBlank()) {
            binding.layoutPokemon.error = getString(R.string.erro_nome_vazio)
            return
        }

        findNavController().navigate(
            BuscaFragmentDirections.actionBuscaFragmentToDetalhesPokemonFragment(pokemon)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
