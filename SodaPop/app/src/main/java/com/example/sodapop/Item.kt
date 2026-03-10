package com.example.sodapop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// Parcelize simplifica y automatiza la serialización de objetos
@Parcelize
data class Item(
    val id: Long,
    val nombre: String,
    val foto: String,
    val nivelDificultad: String,
    val tiempoEstimado: Int,
    val descripcion: String,
    val guardada: Boolean
) : Parcelable
