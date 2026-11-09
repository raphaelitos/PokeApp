package com.example.pokeapp.repository.api

import com.example.pokeapp.repository.api.client.PokemonClient
import com.example.pokeapp.repository.api.model.PokemonEntity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository {

    private val service = PokemonClient.createPokemonService()

    fun buscarPokemon(
        pokemon: String,
        callback: (PokemonEntity?, String?) -> Unit
    ) {

        service.getInfoPokemon(pokemon).enqueue(object : Callback<PokemonEntity> {

            override fun onResponse(
                call: Call<PokemonEntity>,
                response: Response<PokemonEntity>
            ) {

                val poke = response.body()

                when {
                    !response.isSuccessful ->
                        callback(null, "Erro HTTP: ${response.code()}")

                    poke == null ->
                        callback(null, "Resposta vazia da API.")

                    poke.erro ->
                        callback(null, "Pokemon não encontrado.")

                    poke.pokemon.isNullOrBlank()->
                        callback(null, "Resposta inválida da API.")

                    else ->
                        callback(poke, null)
                }
            }

            override fun onFailure(
                call: Call<PokemonEntity>,
                t: Throwable
            ) {
                callback(null, "Erro de conexão.")
            }
        })
    }
}