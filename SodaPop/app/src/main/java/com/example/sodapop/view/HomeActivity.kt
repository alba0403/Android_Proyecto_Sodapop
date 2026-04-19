package com.example.sodapop.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sodapop.R
import com.example.sodapop.model.Receta
import com.example.sodapop.model.RetrofitReceta
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

interface VeuListener {
    fun onVeuChanged(activada: Boolean)
}

class HomeActivity : AppCompatActivity(), VeuListener {

    private lateinit var recognizer: SpeechRecognizer
    private lateinit var recognizerIntent: Intent
    private lateinit var bottomNav: BottomNavigationView

    // Launcher per demanar el permis del micro
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) initSpeechRecognizer()
            else Toast.makeText(this, "Permís de micròfon denegat", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // missatge de benvinguda
        val bienvenida = findViewById<TextView>(R.id.bienvenida)
        val username = intent.getStringExtra("nom_registrat")
        bienvenida.text = if (username.isNullOrBlank()) "Hola!" else "Hola, $username!"

        findViewById<Button>(R.id.btn_buscador).setOnClickListener {
            startActivity(Intent(this, BuscadorActivity::class.java))
        }

        // RecyclerView de receptes recomanades
        val recycler = findViewById<RecyclerView>(R.id.recyclerRecomendadas)
        recycler.layoutManager = LinearLayoutManager(this)
        val recetasRecomendadas = mutableListOf<Receta>()
        val adapter = RecetaAdapter(recetasRecomendadas) { receta ->
            Toast.makeText(this, "Recomendada: ${receta.nombre}", Toast.LENGTH_SHORT).show()
        }
        recycler.adapter = adapter

        lifecycleScope.launch {
            try {
                val response = RetrofitReceta.API().llistaReceptes()
                if (response.isSuccessful) {
                    response.body()?.let {
                        recetasRecomendadas.addAll(it.take(3))
                        adapter.notifyDataSetChanged()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@HomeActivity, "Error de connexió", Toast.LENGTH_SHORT).show()
            }
        }

        // Bottom navigation
        bottomNav = findViewById(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            val fragment: Fragment? = when (item.itemId) {
                R.id.nav_lesmevesreceptes -> LesMevesReceptesFragment()
                R.id.nav_perfil -> PerfilFragment()
                R.id.nav_rebost -> FragmentRebost()
                R.id.nav_grafics -> GraficsFragment()
                else -> null
            }
            if (fragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()
            }
            true
        }

        // Estat inicial del botó del microfon, per a que es guardi la prferencia al tornar a obrir l'app
        val btnVeu = findViewById<ImageButton>(R.id.btnVeu)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (prefs.getBoolean("veu_activada", false)) {
            btnVeu.visibility = View.VISIBLE
            initSpeechRecognizer()
            btnVeu.setOnClickListener {
                recognizer.startListening(recognizerIntent)
                Toast.makeText(this, "Escoltant...", Toast.LENGTH_SHORT).show()
            }
        } else {
            btnVeu.visibility = View.GONE
        }
    }

    private fun initSpeechRecognizer() {
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)

        recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ca-ES")
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val spoken = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.get(0)
                    ?.lowercase()
                handleVoiceCommand(spoken)
            }
            override fun onError(error: Int) {
                Toast.makeText(this@HomeActivity, "No t'he entès, torna-ho a intentar", Toast.LENGTH_SHORT).show()
            }
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    // funció on indiquem totes les paraules possibles per dir al microfon
    private fun handleVoiceCommand(command: String?) {
        when {
            command?.contains("receptes") == true
                    || command?.contains("recetas") == true
                    || command?.contains("les meves receptes") == true
                    || command?.contains("mis recetas") == true
                    || command?.contains("favoritas") == true
                    || command?.contains("favorits") == true -> {
                bottomNav.selectedItemId = R.id.nav_lesmevesreceptes
            }
            command?.contains("perfil") == true -> {
                bottomNav.selectedItemId = R.id.nav_perfil
            }
            command?.contains("rebost") == true
                    || command?.contains("despensa") == true
                    || command?.contains("nevera") == true
                    || command?.contains("alacena") == true -> {
                bottomNav.selectedItemId = R.id.nav_rebost
            }
            command?.contains("gràfics") == true
                    || command?.contains("grafics") == true
                    || command?.contains("estadisticas") == true
                    || command?.contains("gràficos") == true
                    || command?.contains("estadístiques") == true -> {
                bottomNav.selectedItemId = R.id.nav_grafics
            }
            else -> {
                Toast.makeText(this, "Ordre no reconeguda: \"$command\"", Toast.LENGTH_SHORT).show()
            }
        }
    }
        // si desactivem/activem el switch desapareix/apareix el imagebutton sense tancar l'app
        override fun onVeuChanged(activada: Boolean) {
            val btnVeu = findViewById<ImageButton>(R.id.btnVeu)
            if (activada) {
                btnVeu.visibility = View.VISIBLE
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                    initSpeechRecognizer()
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
                btnVeu.setOnClickListener {
                    recognizer.startListening(recognizerIntent)
                    Toast.makeText(this, "Escoltant...", Toast.LENGTH_SHORT).show()
                }
            } else {
                btnVeu.visibility = View.GONE
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        if (::recognizer.isInitialized) recognizer.destroy()
    }
}