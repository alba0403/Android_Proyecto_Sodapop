package com.example.sodapop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider

/**
 * Activity d'Iniciar Sessió.
 *
 * Aquesta Activity s'encarrega de:
 * 1. Mostrar la UI de login (camps de correu i contrasenya, botó d'iniciar sessió).
 * 2. Enviar les dades introduïdes al ViewModel per validar-les.
 * 3. Observar els LiveData del ViewModel per mostrar missatges d'error o navegar a la
 * següent pantalla.
 */
class IniciarSessioActivity : AppCompatActivity() {

    /**
     * Instància del ViewModel.
     * Gestiona els valors dels camps i la lògica de validació.
     */
    private lateinit var viewModel: IniciarSessioViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciar_sessio)

        // Inicialització del ViewModel
        viewModel = ViewModelProvider(this)[IniciarSessioViewModel::class.java]

        // Referenciem els components de la UI
        val editCorreo = findViewById<EditText>(R.id.correu)
        val editContrasenya = findViewById<EditText>(R.id.contrasenya)
        val btnIniciar = findViewById<Button>(R.id.btnIniciarSessio)

        /**
         * Listener del botó d'iniciar sessió.
         *
         * Quan l'usuari prem el botó:
         * 1. Es recullen els valors dels camps
         * 2. Es crida la funció del ViewModel per validar-los
         * 3. Si tots els camps són correctes, es navega a la homeActivity
         * 4. Si hi ha errors, aquests es gestionen amb els Toasts dels observables d'abaix
         */
        btnIniciar.setOnClickListener {
            viewModel.email.value = editCorreo.text.toString()
            viewModel.contrasenya.value = editContrasenya.text.toString()
            viewModel.validarCamps()

            if (viewModel.isLoginPossible.value == true) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        /**
         * Observables del LiveData per mostrar els missatges d'error amb Toasts.
         * Només s'executen quan hi ha un missatge nou!!!
         */
        viewModel.emailError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.contrasenyaError.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

