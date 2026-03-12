package com.example.sodapop

data class NuevaRecetaRequest(
    val nombre: String,
    val foto: String = "https://tudominio.com/default.jpg", // URL fija, no drawable
    val nivelDificultad: String = "Fàcil",
    val tiempoEstimado: Int = 0,
    val descripcion: String = "",
    val guardada: Boolean = true
)