package com.example.test2.data

data class Recipe(
    val name: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val nutritionalValues: NutritionalValues,
    val scores: Scores
)

data class NutritionalValues(
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val servingSize: String,
    val fiber: Float,
    val sugar: Float,
    val sodium: Int
)

data class Scores(
    val difficulty: Int,
    val tastiness: Int,
    val timeInMinutes: Int
)

data class RecipeRequest(
    val ingredients: List<String>
)

// OpenAI API Models
data class ChatCompletionRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<Message>,
    val temperature: Double = 0.7
)

data class Message(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
) 