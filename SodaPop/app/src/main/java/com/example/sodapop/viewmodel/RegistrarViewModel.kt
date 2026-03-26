package com.example.sodapop.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel per a la pantalla de Registre.
 * La seva feina és guardar les dades i fer les comprovacions (lògica)
 * perquè l'Activity només s'encarregui de pintar la pantalla.
 */
class RegistrarViewModel : ViewModel() {
    /**
     * LiveData per saber si el registre ha anat bé.
     * 1. _registreCorrecte (Privada): És Mutable, així que puc modificar-la des d'aquí dins.
     * 2. registreCorrecte (Pública): És no mutable, així l'Activity només la pot llegir (observar) però no tocar.
     */
    private val _registreCorrecte = MutableLiveData<Boolean>(false)
    val registreCorrecte: LiveData<Boolean> = _registreCorrecte

    /**
     * LiveData per gestionar els missatges d'error.
     * Si alguna dada està malament, posarem aquí el text de l'error
     * i l'Activity el mostrarà
     */
    private val _missatgeError = MutableLiveData<String>()
    val missatgeError: LiveData<String> = _missatgeError

    /**
     * Funció que rep les dades que ha escrit l'usuari i mira si són vàlides.
     * En lloc de fer els "if/else" a l'Activity, els fem aquí.
     *
     * @param nom El nom de l'usuari.
     * @param cognoms Els cognoms.
     * @param correu Ha de tenir '@' i '.'.
     * @param contrasenya Mínim 6 caràcters.
     * @param repetirContrasenya Ha de ser igual que la contrasenya.
     */
    fun validarDades(nom: String, cognoms: String, correu: String, contrasenya: String, repetirContrasenya: String) {
        when {
            nom.isBlank() -> {
                _missatgeError.value = "El nom no pot estar buit"
            }
            cognoms.isBlank() -> {
                _missatgeError.value = "Els cognoms no poden estar buits"
            }
            !correu.contains("@") || !correu.contains(".") -> {
                _missatgeError.value = "El correu no és vàlid"
            }
            contrasenya.length < 6 -> {
                _missatgeError.value = "La contrasenya ha de tenir almenys 6 caràcters"
            }
            contrasenya != repetirContrasenya -> {
                _missatgeError.value = "Les contrasenyes no coincideixen"
            }
            else -> {
                _registreCorrecte.value = true
            }
        }
    }
}