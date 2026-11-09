package com.example.pokeapp.repository.api.client

import com.example.pokeapp.repository.api.service.PokemonService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonClient {
    companion object {
        private lateinit var INSTANCE: Retrofit

        private const val BASE_URL = "https://pokeapi.co/"

        private fun getClientInstance(): Retrofit {

            if (!::INSTANCE.isInitialized) {

                val http = OkHttpClient.Builder()

                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(http.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }

            return INSTANCE
        }

        fun createPokemonService(): PokemonService {
            return getClientInstance().create(PokemonService::class.java)
        }
    }
}