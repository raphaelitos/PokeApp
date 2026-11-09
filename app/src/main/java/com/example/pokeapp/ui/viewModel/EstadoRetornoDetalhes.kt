package com.example.pokeapp.ui.viewModel

import com.example.pokeapp.repository.api.model.PokemonEntity

// Possíveis estados da consulta.
sealed class EstadoRetornoDetalhes {

    object Inicial : EstadoRetornoDetalhes()

    object Carregando : EstadoRetornoDetalhes()

    data class Sucesso(val endereco: PokemonEntity) : EstadoRetornoDetalhes()

    data class Erro(val mensagem: String) : EstadoRetornoDetalhes()
}