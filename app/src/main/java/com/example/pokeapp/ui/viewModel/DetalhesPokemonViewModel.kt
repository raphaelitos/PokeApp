package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokeapp.repository.api.PokemonRepository
import java.util.Locale

class DetalhesPokemonViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _estado_retorno =
        MutableLiveData<EstadoRetornoDetalhes>(
            EstadoRetornoDetalhes.Inicial
        )

    val estado_retorno: LiveData<EstadoRetornoDetalhes> =
        _estado_retorno

    fun buscarPokemon(pokeDigitado: String) {

        val poke = pokeDigitado.trim().lowercase(Locale.ROOT)

        if (poke.isBlank()) {
            _estado_retorno.value =
                EstadoRetornoDetalhes.Erro("Informe um Pokémon válido.")
            return
        }

        _estado_retorno.value = EstadoRetornoDetalhes.Carregando

        repository.buscarPokemon(poke) { pokemon, erro ->

            _estado_retorno.value = if (pokemon != null) {
                EstadoRetornoDetalhes.Sucesso(pokemon)
            } else {
                EstadoRetornoDetalhes.Erro(
                    erro ?: "Erro desconhecido."
                )
            }
        }
    }
}