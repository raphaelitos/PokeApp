package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.pokeapp.databinding.FragmentDetalhesPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.ui.viewModel.DetalhesPokemonViewModel
import com.example.pokeapp.ui.viewModel.EstadoRetornoDetalhes

class DetalhesPokemonFragment : Fragment() {

    private var _binding: FragmentDetalhesPokemonBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetalhesPokemonViewModel by viewModels()

    private val args: DetalhesPokemonFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetalhesPokemonBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        // Observa os estados disponibilizados pela ViewModel.
        observarEstado()

        // Solicita a consulta somente se ainda não foi iniciada.
        if (viewModel.estado_retorno.value == EstadoRetornoDetalhes.Inicial) {
            viewModel.buscarPokemon(args.pokemonKey)
        }

        binding.btnVoltarDetalhes.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observarEstado() {

        viewModel.estado_retorno.observe(viewLifecycleOwner) { estado ->

            when (estado) {

                EstadoRetornoDetalhes.Inicial -> {
                    binding.tvNomeSelecionado.text = ""
                }

                EstadoRetornoDetalhes.Carregando -> {
                    binding.tvNomeSelecionado.text =
                        "Carregando informações do Pokémon..."
                }

                is EstadoRetornoDetalhes.Sucesso -> {
                    binding.tvNomeSelecionado.text =
                        formatarPokemon(estado.pokemon)
                }

                is EstadoRetornoDetalhes.Erro -> {
                    binding.tvNomeSelecionado.text =
                        estado.mensagem
                }
            }
        }
    }

    private fun formatarPokemon(pokemon: PokemonEntity): String {

        val tipos = pokemon.types.joinToString(", ") {
            it.type.name
        }

        val estatisticas = pokemon.stats.joinToString("\n") {
            "${it.stat.name}: ${it.baseStat}"
        }

        return """
            Nome: ${pokemon.name}
            ID: ${pokemon.id}

            Altura: ${pokemon.height / 10.0} m
            Peso: ${pokemon.weight / 10.0} kg

            Tipos:
            $tipos

            Estatísticas:
            $estatisticas
        """.trimIndent()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}