package com.example.pokeapp.repository.api

import com.example.pokeapp.repository.api.client.PokemonClient
import com.example.pokeapp.repository.api.model.PokemonEntity
import com.example.pokeapp.repository.api.model.PokemonListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonRepository {

    private val service = PokemonClient.createPokemonService()

    // Consulta um pokemon individualmente.
    fun buscarPokemon(
        pokemon: String,
        callback: (PokemonEntity?, String?) -> Unit
    ) {
        service.getInfoPokemon(pokemon).enqueue(
            object : Callback<PokemonEntity> {

                override fun onResponse(
                    call: Call<PokemonEntity>,
                    response: Response<PokemonEntity>
                ) {
                    val poke = response.body()

                    when {
                        response.code() == 404 ->
                            callback(null, "Pokémon não encontrado.")

                        !response.isSuccessful ->
                            callback(null, "Erro HTTP: ${response.code()}")

                        poke == null ->
                            callback(null, "Resposta vazia da API.")

                        poke.id <= 0 || poke.name.isBlank() ->
                            callback(null, "Resposta inválida da API.")

                        else ->
                            callback(poke, null)
                    }
                }

                override fun onFailure(
                    call: Call<PokemonEntity>,
                    t: Throwable
                ) {
                    callback(null, "Falha na consulta do Pokémon.")
                }
            }
        )
    }

    // Consulta uma página da lista de Pokémon.
    fun listarPokemons(
        limit: Int,
        offset: Int,
        callback: (PokemonListResponse?, String?) -> Unit
    ) {
        service.getListaPokemon(limit, offset).enqueue(
            object : Callback<PokemonListResponse> {

                override fun onResponse(
                    call: Call<PokemonListResponse>,
                    response: Response<PokemonListResponse>
                ) {
                    val pagina = response.body()

                    when {
                        !response.isSuccessful ->
                            callback(null, "Erro HTTP: ${response.code()}")

                        pagina == null ->
                            callback(null, "Resposta vazia da API.")

                        else ->
                            callback(pagina, null)
                    }
                }

                override fun onFailure(
                    call: Call<PokemonListResponse>,
                    t: Throwable
                ) {
                    callback(null, "Falha ao carregar os Pokémon.")
                }
            }
        )
    }
}