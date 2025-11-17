package com.example.pokemons.repositories

import com.example.pokemons.models.Pokemon
import com.example.pokemons.models.PokemonBasic
import com.example.pokemons.services.PokemonApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class PokemonRepository {
    private val api: PokemonApiService = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PokemonApiService::class.java)

    suspend fun getPokemonList(limit: Int = 20): List<PokemonBasic> {
        return api.getPokemonList(limit).results
    }

    suspend fun getPokemonDetails(name: String): Pokemon {
        return api.getPokemon(name)
    }
}