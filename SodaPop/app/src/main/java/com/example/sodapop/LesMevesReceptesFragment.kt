package com.example.sodapop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LesMevesReceptesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecetaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_les_meves_receptes, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Adapter inicial con clics
        adapter = RecetaAdapter(DataSource.recetas) { receta ->
            Toast.makeText(requireContext(), "Has clicat: ${receta.nombre}", Toast.LENGTH_SHORT).show()
        }

        recyclerView.adapter = adapter

        // Ejemplo: filtrar recetas que contengan "s"
        val recetasFiltradas = DataSource.recetas.filter { it.nombre.contains("s", ignoreCase = true) }

        // Actualizar el Adapter con recetas filtradas
        adapter = RecetaAdapter(recetasFiltradas) { receta ->
            Toast.makeText(requireContext(), "Has clicat: ${receta.nombre}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        return view
    }
}
