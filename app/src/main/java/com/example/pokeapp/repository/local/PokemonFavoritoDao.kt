package com.example.pokeapp.repository.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonFavoritoDao {
    @Query("SELECT * FROM pokemon_favoritos ORDER BY id ASC")
    fun observarTodos(): LiveData<List<PokemonFavorito>>

    @Query("SELECT EXISTS(SELECT 1 FROM pokemon_favoritos WHERE id = :id)")
    fun observarFavorito(id: Int): LiveData<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(pokemon: PokemonFavorito)

    @Query("DELETE FROM pokemon_favoritos WHERE id = :id")
    suspend fun remover(id: Int)
}

