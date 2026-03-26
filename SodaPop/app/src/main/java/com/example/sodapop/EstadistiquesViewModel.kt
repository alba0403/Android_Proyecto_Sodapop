package com.example.sodapop

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EstadistiquesViewModel : ViewModel() {

    private val _dades = MutableLiveData<EstadistiquesData>()
    val dades: LiveData<EstadistiquesData> = _dades

    private var dadesActuals = EstadistiquesData()
    private var tempsInici: Long = 0L

    init {
        EstadistiquesRepository.recuperar { carregat ->
            dadesActuals = carregat
            _dades.postValue(dadesActuals)
        }
    }

    fun registrarEntrada() {
        tempsInici = System.currentTimeMillis()
        dadesActuals = dadesActuals.copy(
            vegadesEntrada = dadesActuals.vegadesEntrada + 1
        )
        desar()
    }

    fun registrarSortida() {
        val minuts = (System.currentTimeMillis() - tempsInici) / 60000f
        dadesActuals = dadesActuals.copy(
            minutsUs = dadesActuals.minutsUs + minuts
        )
        desar()
    }

    fun registrarReceptaAfegida() {
        dadesActuals = dadesActuals.copy(
            receptesAfegides = dadesActuals.receptesAfegides + 1
        )
        desar()
    }

    fun registrarReceptaEliminada() {
        dadesActuals = dadesActuals.copy(
            receptesEliminades = dadesActuals.receptesEliminades + 1
        )
        desar()
    }

    private fun desar() {
        _dades.postValue(dadesActuals)
        EstadistiquesRepository.guardar(dadesActuals)
    }

    fun calcularCO2(): Float = dadesActuals.minutsUs * 0.00023f
}