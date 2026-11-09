package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokeapp.repository.api.PokemonRepository


class DetalhesPokemonViewModel: ViewModel(){
    private val repository = PokemonRepository()

    private val _estado_retorno = MutableLiveData<EstadoRetornoDetalhes>(EstadoRetornoDetalhes.Inicial)

    val estado_retorno: LiveData<EstadoRetornoDetalhes> = _estado_retorno

    fun buscarPokemon(pokeDigitado: String) {

        val poke = pokeDigitado.trim().lowercase()

        _estado_retorno.value = EstadoRetornoDetalhes.Carregando

        repository.buscarPokemon(poke) { endereco, erro ->

            _estado_retorno.value = if (endereco != null) {
                EstadoRetornoDetalhes.Sucesso(endereco)
            } else {
                EstadoRetornoDetalhes.Erro(erro ?: "Erro desconhecido.")
            }
        }
    }
}