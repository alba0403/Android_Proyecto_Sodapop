package com.example.sodapop.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.sodapop.R
import com.example.sodapop.viewmodel.RegistrarViewModel

class RegistrarActivity : AppCompatActivity() {

    /**
     * Instància del ViewModel.
     * Utilitzem 'by viewModels()' perquè Android la creï i la gestioni automàticament.
     * Això ens permet connectar amb la lògica.
     */
    private val viewModel: RegistrarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        // Referencias a la UI
        val editNom = findViewById<EditText>(R.id.nom_registrat)
        val editCognoms = findViewById<EditText>(R.id.cognoms)
        val editCorreu = findViewById<EditText>(R.id.correu)
        val editContrasenya = findViewById<EditText>(R.id.contrasenya)
        val editRepetir = findViewById<EditText>(R.id.repetcontrasenya)
        val btnCrear = findViewById<Button>(R.id.CrearCompte)

        /**
         * Quan l'usuari clica el botó, no comprovem res aquí.
         * Simplement agafem el text i l'enviem al ViewModel perquè faci la feina.
         */
        btnCrear.setOnClickListener {
            viewModel.validarDades(
                editNom.text.toString(),
                editCognoms.text.toString(),
                editCorreu.text.toString(),
                editContrasenya.text.toString(),
                editRepetir.text.toString()
            )
        }

        /**
         * Observer: Validació correcte.
         * Cada vegada que el ViewModel canviï aquest valor, s'executarà aquest codi.
         * Si és true, vol dir que tot ha anat bé i canviem de pantalla.
         */
        viewModel.registreCorrecte.observe(this) { correcte ->
            if (correcte) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("nom_registrat", editNom.text.toString())
                startActivity(intent)
            }
        }

        /**
         * Observer 2: Gestió d'errors.
         * Si el ViewModel detecta un error, ens avisa aquí i nosaltres només l'ensenyem.
         */
        viewModel.missatgeError.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}