package com.example.pokeapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokeapp.R
import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.repository.favoritos.PokemonFavorito
import com.example.pokeapp.repository.favoritos.PokemonFavoritosRepository
import com.google.firebase.firestore.ListenerRegistration
import java.util.Locale

enum class AcaoFavorito { ADICIONADO, REMOVIDO }

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PokemonFavoritosRepository()

    private val _estadoLista = MutableLiveData(EstadoFavoritos())
    val estadoLista: LiveData<EstadoFavoritos> = _estadoLista
    private var registroLista: ListenerRegistration? = null

    private val registrosDetalhe = mutableMapOf<Int, ListenerRegistration>()
    private val estadosDetalhe = mutableMapOf<Int, MutableLiveData<Boolean?>>()

    private val _operacaoEmAndamento = MutableLiveData(false)
    val operacaoEmAndamento: LiveData<Boolean> = _operacaoEmAndamento

    private val _erro = MutableLiveData<String?>(null)
    val erro: LiveData<String?> = _erro

    private val _acaoConcluida = MutableLiveData<AcaoFavorito?>(null)
    val acaoConcluida: LiveData<AcaoFavorito?> = _acaoConcluida

    fun iniciarObservacaoLista() {
        if (registroLista != null) return
        _estadoLista.value = EstadoFavoritos()
        registroLista = repository.observarTodos(
            aoReceber = { itens -> _estadoLista.value = EstadoFavoritos(itens, false) },
            aoErro = { _estadoLista.value = EstadoFavoritos(
                itens = _estadoLista.value?.itens.orEmpty(),
                carregando = false,
                erro = getApplication<Application>().getString(R.string.erro_carregar_favoritos)
            ) }
        )
    }

    fun pararObservacaoLista() {
        registroLista?.remove()
        registroLista = null
    }

    fun reiniciarObservacaoLista() {
        pararObservacaoLista()
        iniciarObservacaoLista()
    }

    fun observarFavorito(id: Int): LiveData<Boolean?> {
        estadosDetalhe[id]?.let { return it }
        val estado = MutableLiveData<Boolean?>(null)
        estadosDetalhe[id] = estado
        val registro = repository.observarFavorito(
            id,
            aoReceber = { existe -> estado.value = existe },
            aoErro = {
                _erro.value = getApplication<Application>().getString(R.string.erro_consultar_favorito)
            }
        )
        if (registro != null) registrosDetalhe[id] = registro
        return estado
    }

    fun pararObservacaoFavorito(id: Int) {
        registrosDetalhe.remove(id)?.remove()
        estadosDetalhe.remove(id)
    }

    fun alternarFavorito(pokemon: PokemonEntity, jaFavorito: Boolean) {
        if (_operacaoEmAndamento.value == true) return
        _operacaoEmAndamento.value = true
        val acao = if (jaFavorito) AcaoFavorito.REMOVIDO else AcaoFavorito.ADICIONADO
        val retorno: (Result<Unit>) -> Unit = { resultado ->
            _operacaoEmAndamento.value = false
            if (resultado.isSuccess) {
                _acaoConcluida.value = acao
            } else {
                _erro.value = getApplication<Application>().getString(R.string.erro_atualizar_favoritos)
            }
        }
        if (jaFavorito) {
            repository.remover(pokemon.id, retorno)
        } else {
            repository.adicionar(
                PokemonFavorito(pokemon.id, pokemon.name.lowercase(Locale.ROOT)),
                retorno
            )
        }
    }

    fun removerFavorito(id: Int) {
        if (_operacaoEmAndamento.value == true) return
        _operacaoEmAndamento.value = true
        repository.remover(id) { resultado ->
            _operacaoEmAndamento.value = false
            if (resultado.isSuccess) {
                _acaoConcluida.value = AcaoFavorito.REMOVIDO
            } else {
                _erro.value = getApplication<Application>().getString(R.string.erro_atualizar_favoritos)
            }
        }
    }

    fun limparErro() {
        _erro.value = null
    }

    fun consumirAcaoConcluida() {
        _acaoConcluida.value = null
    }

    override fun onCleared() {
        pararObservacaoLista()
        registrosDetalhe.values.forEach { it.remove() }
        registrosDetalhe.clear()
        estadosDetalhe.clear()
        super.onCleared()
    }
}
