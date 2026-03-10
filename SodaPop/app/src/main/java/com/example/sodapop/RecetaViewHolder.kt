package com.example.sodapop

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecetaViewHolder(
    itemView: View,
    private val onItemClick: (Receta) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val imagen: ImageView = itemView.findViewById(R.id.imagenReceta)
    private val nombre: TextView = itemView.findViewById(R.id.nombreReceta)

    fun bind(receta: Receta) {
        nombre.text = receta.nombre
        imagen.setImageResource(receta.imagen)

        itemView.setOnClickListener {
            onItemClick(receta)
        }
    }
}