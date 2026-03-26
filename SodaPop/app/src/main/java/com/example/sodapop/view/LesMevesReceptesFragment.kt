package com.example.sodapop.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sodapop.services.NuevaRecetaRequest
import com.example.sodapop.R
import com.example.sodapop.model.Receta
import com.example.sodapop.view.RecetaAdapter
import com.example.sodapop.model.RetrofitReceta
import com.example.sodapop.viewmodel.EstadistiquesViewModel
import kotlinx.coroutines.launch

class LesMevesReceptesFragment : Fragment() {

    private val estadistiquesVM: EstadistiquesViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecetaAdapter
    private lateinit var buscadorReceptes: EditText
    private lateinit var botonAñadir: Button
    private lateinit var botonEliminar: Button

    private var recetaSeleccionada: Receta? = null
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
        buscadorReceptes.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
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
                    val response = RetrofitReceta.Companion.API().crearRecepta(nuevaReceta)
                    if (response.isSuccessful) {
                        response.body()?.let { creada ->
                            todasLasRecetas.add(creada)
                            mesMevesRecetas.add(creada)
                            adapter.notifyItemInserted(mesMevesRecetas.size - 1)
                            buscadorReceptes.text.clear()
                            Toast.makeText(requireContext(), "Recepta afegida!", Toast.LENGTH_SHORT).show()

                            estadistiquesVM.registrarReceptaAfegida()
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

            estadistiquesVM.registrarReceptaEliminada()

            lifecycleScope.launch {
                try {
                    val response = RetrofitReceta.Companion.API().eliminarRecepta(recetaSeleccionada!!.id)
                    if (response.isSuccessful) {
                        todasLasRecetas.remove(recetaSeleccionada)
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

    override fun onResume() {
        super.onResume()
        estadistiquesVM.registrarEntrada()
    }

    override fun onPause() {
        super.onPause()
        estadistiquesVM.registrarSortida()
    }

    private fun cargarMisRecetas() {
        lifecycleScope.launch {
            try {
                val response = RetrofitReceta.Companion.API().llistaReceptes()
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