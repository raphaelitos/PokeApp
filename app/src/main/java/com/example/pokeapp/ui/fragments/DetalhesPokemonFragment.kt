package com.example.pokeapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentDetalhesPokemonBinding
import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.ui.viewModel.DetalhesPokemonViewModel
import com.example.pokeapp.ui.viewModel.EstadoRetornoDetalhes
import com.example.pokeapp.ui.viewModel.FavoritosViewModel

class DetalhesPokemonFragment : Fragment() {

    private var _binding: FragmentDetalhesPokemonBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetalhesPokemonViewModel by viewModels()
    private val favoritosViewModel: FavoritosViewModel by viewModels()
    private val args: DetalhesPokemonFragmentArgs by navArgs()

    private var idFavoritoObservado: Int? = null
    private var jaFavorito: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhesPokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFavorito.isEnabled = false
        binding.btnFavorito.visibility = View.GONE

        favoritosViewModel.erro.observe(viewLifecycleOwner) { mensagem ->
            if (mensagem != null) {
                Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
                binding.btnFavorito.isEnabled = true
                favoritosViewModel.limparErro()
            }
        }

        viewModel.estado_retorno.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                EstadoRetornoDetalhes.Inicial -> {
                    binding.tvNomeSelecionado.text = ""
                    binding.imgDetalhesPokemon.visibility = View.GONE
                    binding.btnFavorito.visibility = View.GONE
                }
                EstadoRetornoDetalhes.Carregando -> {
                    binding.tvNomeSelecionado.text =
                        getString(R.string.mensagem_carregando_detalhes)
                    binding.imgDetalhesPokemon.visibility = View.GONE
                    binding.btnFavorito.visibility = View.GONE
                }
                is EstadoRetornoDetalhes.Sucesso -> {
                    apresentarPokemon(estado.pokemon)
                }
                is EstadoRetornoDetalhes.Erro -> {
                    binding.tvNomeSelecionado.text = estado.mensagem
                    binding.imgDetalhesPokemon.visibility = View.GONE
                    binding.btnFavorito.visibility = View.GONE
                }
            }
        }

        if (viewModel.estado_retorno.value == EstadoRetornoDetalhes.Inicial) {
            viewModel.buscarPokemon(args.pokemonKey)
        }

        binding.btnVoltarDetalhes.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun apresentarPokemon(pokemon: PokemonEntity) {
        binding.tvNomeSelecionado.text = formatarPokemon(pokemon)
        binding.imgDetalhesPokemon.visibility = View.VISIBLE
        binding.imgDetalhesPokemon.contentDescription = getString(
            R.string.descricao_imagem_pokemon_nome, pokemon.name
        )
        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/" +
                "sprites/pokemon/other/official-artwork/${pokemon.id}.png"
        Glide.with(binding.imgDetalhesPokemon)
            .load(url)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.imgDetalhesPokemon)

        binding.btnFavorito.visibility = View.VISIBLE
        if (idFavoritoObservado != pokemon.id) {
            idFavoritoObservado = pokemon.id
            binding.btnFavorito.isEnabled = false
            favoritosViewModel.observarFavorito(pokemon.id)
                .observe(viewLifecycleOwner) { favorito ->
                    jaFavorito = favorito
                    binding.btnFavorito.text = getString(
                        if (favorito) R.string.remover_favorito else R.string.adicionar_favorito
                    )
                    binding.btnFavorito.isEnabled = true
                }
        }
        binding.btnFavorito.setOnClickListener {
            binding.btnFavorito.isEnabled = false
            favoritosViewModel.alternarFavorito(pokemon, jaFavorito)
        }
    }

    private fun formatarPokemon(pokemon: PokemonEntity): String {
        val tipos = pokemon.types.joinToString(", ") { it.type.name }
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

            Estatisticas:
            $estatisticas
        """.trimIndent()
    }

    override fun onDestroyView() {
        idFavoritoObservado = null
        _binding = null
        super.onDestroyView()
    }
}
