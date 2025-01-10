package com.example.test2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.test2.api.ApiConfig
import com.example.test2.data.*
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun findRecipes(ingredients: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val prompt = createPrompt(ingredients)
                val request = ChatCompletionRequest(
                    model = "gpt-4o-mini",
                    messages = listOf(Message("user", prompt)),
                    temperature = 0.7
                )
                
                try {
                    val response = ApiConfig.openAIService.getChatCompletion(request)
                    val rawContent = response.choices.firstOrNull()?.message?.content
                    println("Raw API Response: $rawContent")
                    
                    if (rawContent != null) {
                        try {
                            val jsonString = rawContent.trim()
                                .removePrefix("```json")
                                .removePrefix("```")
                                .removeSuffix("```")
                                .trim()
                            
                            val recipes = Gson().fromJson(jsonString, Array<Recipe>::class.java).toList()
                            _recipes.value = recipes
                        } catch (e: Exception) {
                            println("JSON Parsing Error: ${e.message}")
                            println("Attempted to parse: $rawContent")
                            _error.value = "Failed to parse recipes: ${e.message}"
                        }
                    } else {
                        _error.value = "No recipes found in response"
                    }
                } catch (e: Exception) {
                    println("API Call Error: ${e.message}")
                    _error.value = "API call failed: ${e.message}"
                }
            } catch (e: Exception) {
                println("General Error: ${e.message}")
                _error.value = "Error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun createPrompt(ingredients: List<String>): String {
        return """
        You are a JSON API that returns recipes. Return ONLY a JSON array and nothing else - no explanations, no text, just the JSON array.
        
        IMPORTANT RULES:
        1. Use ONLY ingredients from the provided list: ${ingredients.joinToString(", ")}
        2. Recipes can use all OR ANY SUBSET of these ingredients
        3. Do not suggest or use any ingredients that are not in the list
        4. Basic seasonings (salt, pepper, common spices) are allowed
        5. Provide accurate nutritional values per serving
        6. Return at least 5 different recipes, varying in complexity and ingredients used
        
        Return the recipes in this JSON format:
        [
          {
            "name": "Recipe Name",
            "ingredients": ["ingredient1", "ingredient2"],
            "instructions": ["step1", "step2"],
            "nutritionalValues": {
              "calories": 300,
              "protein": 20.5,
              "carbs": 30.5,
              "fat": 10.5,
              "servingSize": "1 portion",
              "fiber": 3.2,
              "sugar": 2.1,
              "sodium": 400
            },
            "scores": {
              "difficulty": 3,
              "tastiness": 4,
              "timeInMinutes": 45
            }
          }
        ]
        
        Remember:
        - Return ONLY the JSON array
        - Can use ALL or SUBSET of provided ingredients
        - No ingredients allowed that aren't in the provided list (except basic seasonings)
        - Include detailed nutritional information per serving
        - Provide diverse recipes with different combinations of the ingredients
        - Return at least 5 recipes if possible
        """.trimIndent()
    }
} 