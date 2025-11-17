package com.example.pokemons.uiComposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemons.models.Pokemon
import com.example.pokemons.models.PokemonBasic
import com.example.pokemons.viewModel.PokemonViewModel
import java.util.Locale



@Composable
fun PokemonApp(vm: PokemonViewModel = viewModel()) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentScreen = vm.currentScreen,
                onNavigate = { vm.navigateTo(it) }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when {
                vm.selectedPokemon != null -> {
                    PokemonDetailScreen(
                        pokemon = vm.selectedPokemon!!,
                        onBack = { vm.clearSelection() }
                    )
                }
                vm.currentScreen == "home" -> {
                    PokemonListScreen(
                        pokemons = vm.filteredPokemonList,
                        isLoading = vm.isLoading,
                        error = vm.error,
                        searchQuery = vm.searchQuery,
                        onSearchQueryChange = { vm.updateSearchQuery(it) },
                        onPokemonClick = { vm.loadPokemonDetails(it.name) }
                    )
                }
                vm.currentScreen == "search" -> {
                    SearchScreen(
                        pokemons = vm.filteredPokemonList,
                        searchQuery = vm.searchQuery,
                        onSearchQueryChange = { vm.updateSearchQuery(it) },
                        onPokemonClick = { vm.loadPokemonDetails(it.name) }
                    )
                }
                vm.currentScreen == "profile" -> {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(currentScreen: String, onNavigate: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Accueil") },
            label = { Text("Accueil") },
            selected = currentScreen == "home",
            onClick = { onNavigate("home") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Recherche") },
            label = { Text("Recherche") },
            selected = currentScreen == "search",
            onClick = { onNavigate("search") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profil") },
            label = { Text("Profil") },
            selected = currentScreen == "profile",
            onClick = { onNavigate("profile") }
        )
    }
}

@Composable
fun PokemonListScreen(
    pokemons: List<PokemonBasic>,
    isLoading: Boolean,
    error: String?,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onPokemonClick: (PokemonBasic) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Pokédex",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Rechercher un Pokémon...") },
            leadingIcon = { Icon(Icons.Filled.Search, "Recherche") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Filled.Clear, "Effacer")
                    }
                }
            },
            singleLine = true
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
            pokemons.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucun Pokémon trouvé")
                }
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
                text = pokemon.name.capitalize(),
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

@Composable
fun SearchScreen(
    pokemons: List<PokemonBasic>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onPokemonClick: (PokemonBasic) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Recherche",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Entrez le nom d'un Pokémon...") },
            leadingIcon = { Icon(Icons.Filled.Search, "Recherche") },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Filled.Clear, "Effacer")
                    }
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (searchQuery.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Recherchez un Pokémon",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        } else if (pokemons.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucun Pokémon trouvé pour '$searchQuery'")
            }
        } else {
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

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Profil",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Icon(
            Icons.Filled.Person,
            contentDescription = "Avatar",
            modifier = Modifier
                .size(120.dp)
                .padding(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Dresseur Pokémon",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoRow("Pokémons vus", "150")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoRow("Pokémons capturés", "0")
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoRow("Type favori", "Feu")
            }
        }
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}