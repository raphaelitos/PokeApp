package com.example.pokeapp.repository.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_favoritos")
data class PokemonFavorito(
    @PrimaryKey val id: Int,
    val name: String
)
