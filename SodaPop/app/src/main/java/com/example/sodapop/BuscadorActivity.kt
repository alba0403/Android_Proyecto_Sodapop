package com.example.sodapop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BuscadorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscador)

        val editTextBuscar = findViewById<EditText>(R.id.buscadorIngredientes)
        val botonBuscar = findViewById<Button>(R.id.buscarReceptes)

        botonBuscar.setOnClickListener {
            val query = editTextBuscar.text.toString()

            if (query.isBlank()) {
                Toast.makeText(this, "Escribe algo para buscar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(this, ResultatsActivity::class.java)
            intent.putExtra("search_query", query)
            startActivity(intent)
        }
    }
}