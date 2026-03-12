package com.example.sodapop

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ResultatsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecetaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultats_activity)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val searchQuery = intent.getStringExtra("search_query") ?: ""

        val listaResultados = mutableListOf<Receta>()

        adapter = RecetaAdapter(listaResultados) { receta ->
            Toast.makeText(this, "Has clicat: ${receta.nombre}", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                val response = RetrofitReceta.API().llistaReceptes()
                if (response.isSuccessful) {
                    response.body()?.let { lista ->
                        val filtradas = if (searchQuery.isEmpty()) {
                            lista
                        } else {
                            lista.filter { it.nombre.contains(searchQuery, ignoreCase = true) }
                        }
                        listaResultados.addAll(filtradas)
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@ResultatsActivity, "Error de connexió", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageView>(R.id.resultats_backbutton).setOnClickListener {
            finish()
        }
    }
}