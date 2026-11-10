package com.example.pokeapp.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokeapp.repository.api.PokemonRepository

class ListaPokemonViewModel : ViewModel() {

    private val repository = PokemonRepository()
    private val _estado = MutableLiveData(EstadoListaPokemon())
    val estado: LiveData<EstadoListaPokemon> = _estado

    fun carregarProximaPagina() {
        val atual = _estado.value ?: EstadoListaPokemon()
        if (atual.carregando || !atual.possuiMais) return

        val offsetSolicitado = atual.proximoOffset
        _estado.value = atual.copy(carregando = true, erro = null)

        repository.listarPokemons(limit = 20, offset = offsetSolicitado) { pagina, erro ->
            val estadoAtual = _estado.value ?: atual

            _estado.value = if (pagina != null) {
                estadoAtual.copy(
                    itens = estadoAtual.itens + pagina.results,
                    carregando = false,
                    erro = null,
                    proximoOffset = offsetSolicitado + pagina.results.size,
                    possuiMais = pagina.next != null && pagina.results.isNotEmpty()
                )
            } else {
                estadoAtual.copy(
                    carregando = false,
                    erro = erro ?: "Falha ao carregar a lista."
                )
            }
        }
    }
}
