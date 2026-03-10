package com.example.sodapop

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecetaAdapter(
    private val recetas: List<Receta>,
    private val onItemClick: (Receta) -> Unit
) : RecyclerView.Adapter<RecetaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecetaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return RecetaViewHolder(view, onItemClick)
    }

    override fun getItemCount(): Int = recetas.size

    override fun onBindViewHolder(holder: RecetaViewHolder, position: Int) {
        holder.bind(recetas[position])
    }
}