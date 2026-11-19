package com.example.pokeapp.ui.viewModel

import com.example.pokeapp.repository.favoritos.PokemonFavorito

data class EstadoFavoritos(
    val itens: List<PokemonFavorito> = emptyList(),
    val carregando: Boolean = true,
    val erro: String? = null
)
