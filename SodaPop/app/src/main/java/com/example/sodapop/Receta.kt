package com.example.sodapop

// Este NO lo tocas — es el que te dijo tu profe
data class Receta(
    val id: Long,
    val nombre: String,
    val foto: String,
    val nivelDificultad: String,
    val tiempoEstimado: Int,
    val descripcion: String,
    val guardada: Boolean
)