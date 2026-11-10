package com.example.pokeapp.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.pokeapp.R
import com.example.pokeapp.databinding.FragmentDetalhesPokemonBinding
import com.example.pokeapp.databinding.ItemStatBinding
import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.ui.util.PokemonUi
import com.example.pokeapp.ui.viewModel.DetalhesPokemonViewModel
import com.example.pokeapp.ui.viewModel.EstadoRetornoDetalhes
import com.example.pokeapp.ui.viewModel.FavoritosViewModel
import com.google.android.material.chip.Chip
import kotlin.math.min

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

        favoritosViewModel.erro.observe(viewLifecycleOwner) { mensagem ->
            if (mensagem != null) {
                Toast.makeText(requireContext(), mensagem, Toast.LENGTH_SHORT).show()
                binding.btnFavorito.isEnabled = true
                favoritosViewModel.limparErro()
            }
        }

        viewModel.estado_retorno.observe(viewLifecycleOwner) { estado ->
            when (estado) {
                EstadoRetornoDetalhes.Inicial -> mostrarSomente(Tela.NENHUMA)
                EstadoRetornoDetalhes.Carregando -> mostrarSomente(Tela.CARREGANDO)
                is EstadoRetornoDetalhes.Sucesso -> {
                    mostrarSomente(Tela.CONTEUDO)
                    apresentarPokemon(estado.pokemon)
                }
                is EstadoRetornoDetalhes.Erro -> {
                    binding.txtErroDetalhes.text = estado.mensagem
                    mostrarSomente(Tela.ERRO)
                }
            }
        }

        if (viewModel.estado_retorno.value == EstadoRetornoDetalhes.Inicial) {
            viewModel.buscarPokemon(args.pokemonKey)
        }

        binding.btnTentarNovamenteDetalhes.setOnClickListener {
            viewModel.buscarPokemon(args.pokemonKey)
        }

        binding.btnVoltarDetalhes.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private enum class Tela { NENHUMA, CARREGANDO, CONTEUDO, ERRO }

    private fun mostrarSomente(tela: Tela) {
        binding.progressDetalhes.isVisible = tela == Tela.CARREGANDO
        binding.scrollConteudo.isVisible = tela == Tela.CONTEUDO
        binding.layoutErroDetalhes.isVisible = tela == Tela.ERRO
        binding.btnFavorito.isVisible = tela == Tela.CONTEUDO
    }

    private fun apresentarPokemon(pokemon: PokemonEntity) {
        val context = requireContext()
        val nome = PokemonUi.formatarNome(pokemon.name)
        val tipos = pokemon.types.sortedBy { it.slot }.map { it.type.name }
        val corPrincipal = PokemonUi.corDoTipo(context, tipos.firstOrNull().orEmpty())

        binding.txtNomeDetalhes.text = nome
        binding.txtNumeroDetalhes.text = getString(R.string.numero_pokemon, pokemon.id)
        binding.txtAltura.text = getString(R.string.valor_altura, pokemon.height / 10.0)
        binding.txtPeso.text = getString(R.string.valor_peso, pokemon.weight / 10.0)

        // Cartão de destaque com um tom suave da cor do tipo principal.
        binding.cardDestaque.setCardBackgroundColor(ColorUtils.setAlphaComponent(corPrincipal, 56))

        binding.imgDetalhesPokemon.contentDescription =
            getString(R.string.descricao_imagem_pokemon_nome, nome)
        Glide.with(binding.imgDetalhesPokemon)
            .load(PokemonUi.urlArtwork(pokemon.id))
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.imgDetalhesPokemon)

        apresentarTipos(tipos)
        apresentarEstatisticas(pokemon, corPrincipal)
        configurarFavorito(pokemon)
    }

    private fun apresentarTipos(tipos: List<String>) {
        val context = requireContext()
        binding.chipsTipos.removeAllViews()
        tipos.forEach { tipo ->
            val chip = layoutInflater.inflate(
                R.layout.item_tipo_chip, binding.chipsTipos, false
            ) as Chip
            chip.text = PokemonUi.nomeDoTipo(context, tipo)
            chip.setChipBackgroundColor(
                ColorStateList.valueOf(PokemonUi.corDoTipo(context, tipo))
            )
            binding.chipsTipos.addView(chip)
        }
    }

    private fun apresentarEstatisticas(pokemon: PokemonEntity, cor: Int) {
        val context = requireContext()
        binding.containerStats.removeAllViews()
        pokemon.stats.forEach { stat ->
            val item = ItemStatBinding.inflate(layoutInflater, binding.containerStats, false)
            item.txtStatNome.text = PokemonUi.nomeDoStat(context, stat.stat.name)
            item.txtStatValor.text = stat.baseStat.toString()
            item.progressStat.setIndicatorColor(cor)
            item.progressStat.setProgressCompat(min(stat.baseStat, item.progressStat.max), false)
            binding.containerStats.addView(item.root)
        }
    }

    private fun configurarFavorito(pokemon: PokemonEntity) {
        if (idFavoritoObservado != pokemon.id) {
            idFavoritoObservado = pokemon.id
            binding.btnFavorito.isEnabled = false
            favoritosViewModel.observarFavorito(pokemon.id)
                .observe(viewLifecycleOwner) { favorito ->
                    jaFavorito = favorito
                    atualizarBotaoFavorito(favorito)
                    binding.btnFavorito.isEnabled = true
                }
        }
        binding.btnFavorito.setOnClickListener {
            binding.btnFavorito.isEnabled = false
            favoritosViewModel.alternarFavorito(pokemon, jaFavorito)
        }
    }

    private fun atualizarBotaoFavorito(favorito: Boolean) {
        binding.btnFavorito.setIconResource(
            if (favorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        )
        binding.btnFavorito.contentDescription = getString(
            if (favorito) R.string.remover_favorito else R.string.adicionar_favorito
        )
    }

    override fun onDestroyView() {
        idFavoritoObservado = null
        _binding = null
        super.onDestroyView()
    }
}
