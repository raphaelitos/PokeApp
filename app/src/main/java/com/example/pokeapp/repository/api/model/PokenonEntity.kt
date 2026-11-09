package com.example.pokeapp.repository.api.model

import com.google.gson.annotations.SerializedName

data class PokemonEntity(
    val id: Int = 0,
    val name: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val types: List<PokemonTypeEntry> = emptyList(),
    val stats: List<PokemonStatEntry> = emptyList(),
    val sprites: PokemonSprites = PokemonSprites()
)

data class PokemonTypeEntry(
    val slot: Int = 0,
    val type: PokemonNamedResource = PokemonNamedResource()
)

data class PokemonStatEntry(
    @SerializedName("base_stat")
    val baseStat: Int = 0,

    val stat: PokemonNamedResource = PokemonNamedResource()
)

data class PokemonNamedResource(
    val name: String = "",
    val url: String = ""
)

data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String? = null
)