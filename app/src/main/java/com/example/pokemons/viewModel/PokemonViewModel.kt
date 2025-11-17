package com.example.pokemons.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemons.models.Pokemon
import com.example.pokemons.models.PokemonBasic
import com.example.pokemons.repositories.PokemonRepository
import kotlinx.coroutines.launch

class PokemonViewModel : ViewModel()
{
    private val repo = PokemonRepository()

    var pokemonList by mutableStateOf<List<PokemonBasic>>(emptyList())
        private set

    var selectedPokemon by mutableStateOf<Pokemon?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadPokemonList()
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                pokemonList = repo.getPokemonList(50)
            } catch (e: Exception) {
                error = "Erreur: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadPokemonDetails(name: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                selectedPokemon = repo.getPokemonDetails(name)
            } catch (e: Exception) {
                error = "Erreur: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearSelection() {
        selectedPokemon = null
    }
}