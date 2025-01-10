package com.example.test2.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.test2.data.Recipe

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeCard(recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Ingredients:", style = MaterialTheme.typography.titleMedium)
            recipe.ingredients.forEach { ingredient ->
                Text("• $ingredient")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text("Instructions:", style = MaterialTheme.typography.titleMedium)
            recipe.instructions.forEachIndexed { index, instruction ->
                Text("${index + 1}. $instruction")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Difficulty: ${recipe.scores.difficulty}/5")
                    Text("Tastiness: ${recipe.scores.tastiness}/5")
                    Text("Time: ${recipe.scores.timeInMinutes} min")
                }
                Column {
                    Text("Calories: ${recipe.nutritionalValues.calories}")
                    Text("Protein: ${recipe.nutritionalValues.protein}g")
                    Text("Carbs: ${recipe.nutritionalValues.carbs}g")
                    Text("Fat: ${recipe.nutritionalValues.fat}g")
                }
            }
        }
    }
} 