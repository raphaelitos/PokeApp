package com.example.pokeapp.repository.local

import android.content.Context
import androidx.lifecycle.LiveData

class PokemonFavoritosRepository(context: Context) {
    private val dao = PokemonDatabase.obter(context).favoritosDao()

    val favoritos: LiveData<List<PokemonFavorito>> = dao.observarTodos()

    fun observarFavorito(id: Int): LiveData<Boolean> = dao.observarFavorito(id)

    suspend fun adicionar(pokemon: PokemonFavorito) = dao.inserir(pokemon)

    suspend fun remover(id: Int) = dao.remover(id)
}

