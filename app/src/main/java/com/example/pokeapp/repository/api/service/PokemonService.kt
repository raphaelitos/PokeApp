package com.example.pokeapp.repository.api.service

import com.example.pokeapp.repository.api.model.PokemonEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    @GET("/api/v2/pokemon/{nome}/")
    fun getInfoPokemon(
        @Path("nome") nome: String
    ): Call<PokemonEntity>
}