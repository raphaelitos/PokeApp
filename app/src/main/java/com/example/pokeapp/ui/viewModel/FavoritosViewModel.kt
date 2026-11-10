package com.example.pokeapp.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.repository.local.PokemonFavorito
import com.example.pokeapp.repository.local.PokemonFavoritosRepository
import kotlinx.coroutines.launch
import java.util.Locale

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = PokemonFavoritosRepository(application)
    val favoritos: LiveData<List<PokemonFavorito>> = repository.favoritos

    private val _erro = MutableLiveData<String?>(null)
    val erro: LiveData<String?> = _erro

    fun observarFavorito(id: Int): LiveData<Boolean> = repository.observarFavorito(id)

    fun alternarFavorito(pokemon: PokemonEntity, jaFavorito: Boolean) {
        viewModelScope.launch {
            try {
                if (jaFavorito) {
                    repository.remover(pokemon.id)
                } else {
                    repository.adicionar(
                        PokemonFavorito(
                            id = pokemon.id,
                            name = pokemon.name.lowercase(Locale.ROOT)
                        )
                    )
                }
            } catch (e: Exception) {
                _erro.value = "Nao foi possivel atualizar os favoritos."
            }
        }
    }

    fun limparErro() {
        _erro.value = null
    }
}

