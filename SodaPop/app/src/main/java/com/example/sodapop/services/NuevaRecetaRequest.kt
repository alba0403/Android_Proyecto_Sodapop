package com.example.sodapop.services

data class NuevaRecetaRequest(
    val nombre: String,
    val foto: String = "https://misrecetas.com/default.jpg",
    val nivelDificultad: String = "Media",   // debe ser "Baja", "Media" o "Alta"
    val tiempoEstimado: Int = 30,            // debe ser > 0
    val descripcion: String = "Sin descripción",  // no puede estar vacía
    val guardada: Boolean = true
)