package com.example.sodapop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel per a la pantalla d'Iniciar Sessió.
 * La seva funció és emmagatzemar les dades introduïdes per l'usuari
 * i validar si són correctes. L'Activity només s'encarrega de la UI.
 */
class IniciarSessioViewModel : ViewModel() {

    /**
     * Email i contrasenya introduïdes per l'usuari.
     */
    val email = MutableLiveData<String>()
    val contrasenya = MutableLiveData<String>()

    /**
     * Missatge d'error per l'email i la contrasenya. Null si no hi ha error.
     */
    val emailError = MutableLiveData<String?>()
    val contrasenyaError = MutableLiveData<String?>()

    /**
     * Boolean que indica si és possible iniciar sessió.
     * True si els dos camps són vàlids.
     */
    val isLoginPossible = MutableLiveData<Boolean>()

    /**
     * Valida els camps introduïts per l'usuari.
     *
     * - L'email ha de tenir un format vàlid.
     * - La contrasenya ha de tenir almenys 6 caràcters.
     *
     * Actualitza els LiveData d'error i el isLoginPossible.
     */
    fun validarCamps() {
        val emailValue = email.value ?: ""
        val passwordValue = contrasenya.value ?: ""

        emailError.value = if (!emailValue.contains("@") || !emailValue.contains(".")) {
            "Correu no vàlid"
        } else {
            null
        }

        contrasenyaError.value = if (passwordValue.length < 6) {
            "La contrasenya ha de tenir almenys 6 caràcters"
        } else {
            null
        }

        isLoginPossible.value = emailError.value == null && contrasenyaError.value == null
    }
}
