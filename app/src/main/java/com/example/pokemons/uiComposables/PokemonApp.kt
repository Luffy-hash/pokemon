package com.example.pokemons.uiComposables

import android.view.Surface
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemons.models.Pokemon
import com.example.pokemons.models.PokemonBasic
import com.example.pokemons.viewModel.PokemonViewModel
import java.util.Locale



@Composable
fun PokemonApp(vm: PokemonViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (vm.selectedPokemon != null) {
            PokemonDetailScreen(
                pokemon = vm.selectedPokemon!!,
                onBack = { vm.clearSelection() }
            )
        } else {
            PokemonListScreen(
                pokemons = vm.pokemonList,
                isLoading = vm.isLoading,
                error = vm.error,
                onPokemonClick = { vm.loadPokemonDetails(it.name) }
            )
        }
    }
}

@Composable
fun PokemonListScreen(
    pokemons: List<PokemonBasic>,
    isLoading: Boolean,
    error: String?,
    onPokemonClick: (PokemonBasic) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            error != null -> {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> {
                LazyColumn {
                    items(pokemons) { pokemon ->
                        PokemonListItem(
                            pokemon = pokemon,
                            onClick = { onPokemonClick(pokemon) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonListItem(pokemon: PokemonBasic, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = pokemon.name.capitalize(Locale.ROOT),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun PokemonDetailScreen(pokemon: Pokemon, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour")
        }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = pokemon.sprite.front_sprites,
            contentDescription = pokemon.name,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = pokemon.name.capitalize(),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("ID: ${pokemon.id}", style = MaterialTheme.typography.bodyLarge)
        Text("Taille: ${pokemon.height} m", style = MaterialTheme.typography.bodyLarge)
        Text("Poids: ${pokemon.weight} kg", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Types:", style = MaterialTheme.typography.titleMedium)
        pokemon.types.forEach { typeSlot ->
            Text("• ${typeSlot.type.name.capitalize()}")
        }
    }
}