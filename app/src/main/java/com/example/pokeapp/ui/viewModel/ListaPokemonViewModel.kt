package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokeapp.repository.api.PokemonRepository

class ListaPokemonViewModel : ViewModel() {

    private val repository = PokemonRepository()

    private val _estado = MutableLiveData(
        EstadoListaPokemon()
    )

    val estado: LiveData<EstadoListaPokemon> = _estado

    fun carregarProximaPagina() {
        if (_estado.value?.carregando != true) {
            if (!_estado.value?.possuiMais!!) {
                return
            }

            _estado.value = _estado.value?.copy(
                carregando = true
            )

            repository.listarPokemons(
                limit = 20,
                offset = _estado.value?.proximoOffset ?: 0,
            ) { pagina, erro ->
                _estado.value = if (pagina != null) {
                    _estado.value?.copy(
                        itens = _estado.value?.itens.orEmpty() + pagina.results,
                        carregando = false,
                        erro = null,
                        proximoOffset = _estado.value?.proximoOffset?.plus(20) ?: 0,
                        possuiMais = _estado.value?.possuiMais!! && pagina.next != null)
                }
                else {
                    _estado.value?.copy(
                        carregando = false,
                        erro = erro ?: "Erro desconhecido."
                    )
                }
            }
        }
    }
}