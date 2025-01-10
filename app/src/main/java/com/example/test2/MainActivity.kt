package com.example.test2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.test2.ui.theme.Test2Theme
import com.example.test2.viewmodel.RecipeViewModel
import com.example.test2.ui.components.RecipeCard
import com.example.test2.data.Recipe

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Test2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RecipeScreenContent()
                }
            }
        }
    }
}

@Composable
private fun RecipeScreenContent() {
    val viewModel: RecipeViewModel = viewModel()
    val recipes by viewModel.recipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    RecipeScreen(
        recipes = recipes,
        isLoading = isLoading,
        error = error,
        onFindRecipes = viewModel::findRecipes
    )
}

@Composable
fun RecipeScreen(
    recipes: List<Recipe>,
    isLoading: Boolean,
    error: String?,
    onFindRecipes: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var ingredient by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf(listOf<String>()) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextField(
            value = ingredient,
            onValueChange = { ingredient = it },
            label = { Text("Enter ingredient") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
            ),
            singleLine = true,
            maxLines = 1
        )
        
        Button(
            onClick = {
                if (ingredient.isNotBlank()) {
                    ingredients = ingredients + ingredient
                    ingredient = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Ingredient")
        }

        Text("Current ingredients:", style = MaterialTheme.typography.titleMedium)
        ingredients.forEach { ing ->
            Text("• $ing")
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }

        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Button(
            onClick = { onFindRecipes(ingredients) },
            modifier = Modifier.fillMaxWidth(),
            enabled = ingredients.isNotEmpty() && !isLoading
        ) {
            Text("Find Recipes")
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = recipes,
                key = { recipe -> recipe.name }
            ) { recipe ->
                RecipeCard(recipe = recipe)
            }
        }
    }
}