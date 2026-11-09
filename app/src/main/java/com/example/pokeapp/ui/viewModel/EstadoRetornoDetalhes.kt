package com.example.pokeapp.ui.viewModel

import com.example.pokeapp.repository.api.model.PokemonEntity

sealed class EstadoRetornoDetalhes {

    object Inicial : EstadoRetornoDetalhes()

    object Carregando : EstadoRetornoDetalhes()

    data class Sucesso(
        val pokemon: PokemonEntity
    ) : EstadoRetornoDetalhes()

    data class Erro(
        val mensagem: String
    ) : EstadoRetornoDetalhes()
}