package com.example.pokeapp.repository.api.service

import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.repository.api.model.PokemonListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    // Consulta um Pokémon individualmente.
    @GET("/api/v2/pokemon/{nome}/")
    fun getInfoPokemon(
        @Path("nome") nome: String
    ): Call<PokemonEntity>

    // Consulta uma página da listagem.
    @GET("/api/v2/pokemon/")
    fun getListaPokemon(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Call<PokemonListResponse>
}