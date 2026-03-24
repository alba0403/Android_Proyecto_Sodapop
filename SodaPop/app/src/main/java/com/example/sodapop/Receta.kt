package com.example.sodapop

data class Receta(
    val id: Long,
    val nombre: String,
    val foto: String,
    val nivelDificultad: String,
    val tiempoEstimado: Int,
    val descripcion: String,
    val guardada: Boolean
)