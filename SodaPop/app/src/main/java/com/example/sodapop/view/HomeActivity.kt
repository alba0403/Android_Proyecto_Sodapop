package com.example.sodapop.view

import com.example.sodapop.view.GraficsFragment
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sodapop.view.FragmentRebost
import com.example.sodapop.view.LesMevesReceptesFragment
import com.example.sodapop.view.PerfilFragment
import com.example.sodapop.R
import com.example.sodapop.model.Receta
import com.example.sodapop.view.RecetaAdapter
import com.example.sodapop.model.RetrofitReceta
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        //Mensaje de bienvenida
        val bienvenida = findViewById<TextView>(R.id.bienvenida)
        val username = intent.getStringExtra("nom_registrat")
        val mensaje = if (username.isNullOrBlank()) {
            "Hola!"
        } else {
            "Hola, $username!"
        }
        bienvenida.text = mensaje

        //Botones de buscador y filtro
        findViewById<Button>(R.id.btn_buscador).setOnClickListener {
            startActivity(Intent(this, BuscadorActivity::class.java))
        }

//        findViewById<ImageView>(R.id.backButton).setOnClickListener {
//            startActivity(intent(this, IniciarSessioActivity::class.java))
//        }
        //findViewById<Button>(R.id.btn_filtratge).setOnClickListener {
        //    startActivity(intent(this, FiltratgeActivity::class.java))
        //}

        //RECYCLER DE RECETAS RECOMENDADAS
        val recycler = findViewById<RecyclerView>(R.id.recyclerRecomendadas)
        recycler.layoutManager = LinearLayoutManager(this)

        val recetasRecomendadas = mutableListOf<Receta>()

        val adapter = RecetaAdapter(recetasRecomendadas) { receta ->
            Toast.makeText(this, "Recomendada: ${receta.nombre}", Toast.LENGTH_SHORT).show()
        }

        recycler.adapter = adapter

        lifecycleScope.launch {
            try {
                val response = RetrofitReceta.Companion.API().llistaReceptes()
                if (response.isSuccessful) {
                    response.body()?.let { lista ->
                        recetasRecomendadas.addAll(lista.take(3))
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error de connexió", Toast.LENGTH_SHORT).show()
            }
        }
        //Menu navigation
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNav)

        bottomNav.setOnItemSelectedListener { item ->
            val selectedFragment: Fragment? = when (item.itemId) {
                R.id.nav_lesmevesreceptes -> LesMevesReceptesFragment()
                R.id.nav_perfil -> PerfilFragment()
                R.id.nav_rebost -> FragmentRebost()
                R.id.nav_grafics -> GraficsFragment()
                else -> null
            }

            // Realitza la transacció de Fragment manualment
            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }
            true // Indica que la selecció ha estat gestionada
        }
    }

}