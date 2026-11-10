package com.example.pokeapp.ui.viewModel

import com.example.pokeapp.repository.api.model.PokemonListItem

data class EstadoListaPokemon(
    val itens: List<PokemonListItem> = emptyList(),
    val carregando: Boolean = false,
    val erro: String? = null,
    val proximoOffset: Int = 0,
    val possuiMais: Boolean = true
)