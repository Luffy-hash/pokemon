package com.example.pokemons.models

data class PokemonListResponse(
    val count: Int,
    val results: List<PokemonBasic>
)
