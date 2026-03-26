package com.example.sodapop.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sodapop.R
import com.example.sodapop.model.Receta

class RecetaViewHolder(
    itemView: View,
    private val onItemClick: (Receta) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val imagen: ImageView = itemView.findViewById(R.id.imagenReceta)
    private val nombre: TextView = itemView.findViewById(R.id.nombreReceta)

    fun bind(receta: Receta) {
        nombre.text = receta.nombre

        Glide.with(itemView.context)
            .load(receta.foto)
            .placeholder(R.drawable.recepta_default)
            .error(R.drawable.recepta_default)
            .into(imagen)

        itemView.setOnClickListener {
            onItemClick(receta)
        }
    }
}