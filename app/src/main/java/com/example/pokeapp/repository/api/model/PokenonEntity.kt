package com.example.pokeapp.repository.api.model

import com.google.gson.annotations.SerializedName

data class PokemonEntity(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("height")
    val height: Int,

    @SerializedName("weight")
    val weight: Int,

    @SerializedName("types")
    val types: List<PokemonTypeEntry>,

    @SerializedName("stats")
    val stats: List<PokemonStatEntry>,

    @SerializedName("sprites")
    val sprites: PokemonSprites
)

data class PokemonTypeEntry(
    @SerializedName("slot")
    val slot: Int,

    @SerializedName("type")
    val type: PokemonTypeInfo
)

data class PokemonTypeInfo(
    @SerializedName("name")
    val name: String
)

data class PokemonStatEntry(
    @SerializedName("base_stat")
    val baseStat: Int,

    @SerializedName("stat")
    val stat: PokemonStatInfo
)

data class PokemonStatInfo(
    @SerializedName("name")
    val name: String
)

data class PokemonSprites(
    @SerializedName("front_default")
    val frontDefault: String?
)