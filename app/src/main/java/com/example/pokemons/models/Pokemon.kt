package com.example.pokemons.models

data class Pokemon(
    val id: Int,
    val name: String,
    val height: String,
    val weight: String,
    val sprite: Sprites,
    val types: List<TypeSlot>
)
