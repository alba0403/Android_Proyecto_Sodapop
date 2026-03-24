package com.example.sodapop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class LesMevesReceptesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecetaAdapter
    private lateinit var buscadorReceptes: EditText
    private lateinit var botonAñadir: Button
    private lateinit var botonEliminar: Button

    private val mesMevesRecetas = mutableListOf<Receta>()
    private val todasLasRecetas = mutableListOf<Receta>() // llista completa per el filtre

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_les_meves_receptes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        buscadorReceptes = view.findViewById(R.id.buscadorReceptes)
        botonAñadir = view.findViewById(R.id.botonAñadir)
        botonEliminar = view.findViewById(R.id.botonEliminar)

        adapter = RecetaAdapter(mesMevesRecetas) { receta ->
            recetaSeleccionada = if (recetaSeleccionada == receta) null else receta
            adapter.notifyDataSetChanged()
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        cargarMisRecetas()

        // FILTRO en tiempo real mientras escribes
        buscadorReceptes.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                val texto = s.toString().trim().lowercase()
                mesMevesRecetas.clear()
                if (texto.isEmpty()) {
                    mesMevesRecetas.addAll(todasLasRecetas)
                } else {
                    mesMevesRecetas.addAll(
                        todasLasRecetas.filter { it.nombre.lowercase().contains(texto) }
                    )
                }
                adapter.notifyDataSetChanged()
            }
        })

        // AFEGIR
        botonAñadir.setOnClickListener {
            val nombre = buscadorReceptes.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(requireContext(), "Escriu un nom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaReceta = NuevaRecetaRequest(nombre = nombre)

            lifecycleScope.launch {
                try {
                    val response = RetrofitReceta.API().crearRecepta(nuevaReceta)
                    if (response.isSuccessful) {
                        response.body()?.let { creada ->
                            todasLasRecetas.add(creada)
                            mesMevesRecetas.add(creada)
                            adapter.notifyItemInserted(mesMevesRecetas.size - 1)
                            buscadorReceptes.text.clear()
                            Toast.makeText(requireContext(), "Recepta afegida!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al afegir", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error de connexió", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // ELIMINAR
        botonEliminar.setOnClickListener {
            if (recetaSeleccionada == null) {
                Toast.makeText(requireContext(), "Selecciona una recepta primer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                try {
                    val response = RetrofitReceta.API().eliminarRecepta(recetaSeleccionada!!.id)
                    if (response.isSuccessful) {
                        todasLasRecetas.remove(recetaSeleccionada) // ← eliminar también de la lista completa
                        val index = mesMevesRecetas.indexOf(recetaSeleccionada)
                        mesMevesRecetas.removeAt(index)
                        recetaSeleccionada = null
                        adapter.notifyItemRemoved(index)
                        Toast.makeText(requireContext(), "Recepta eliminada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Error de connexió", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private var recetaSeleccionada: Receta? = null

    private fun cargarMisRecetas() {
        lifecycleScope.launch {
            try {
                val response = RetrofitReceta.API().llistaReceptes()
                if (response.isSuccessful) {
                    response.body()?.let { lista ->
                        todasLasRecetas.clear()
                        todasLasRecetas.addAll(lista)
                        mesMevesRecetas.clear()
                        mesMevesRecetas.addAll(lista)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error de connexió", Toast.LENGTH_SHORT).show()
            }
        }
    }
}