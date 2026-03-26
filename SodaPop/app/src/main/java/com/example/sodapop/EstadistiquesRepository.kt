package com.example.sodapop

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object EstadistiquesRepository {

    private val db = Firebase.firestore
    private const val DOC_ID = "estadistiques_globals"

    fun guardar(dades: EstadistiquesData) {
        db.collection("estadistiques")
            .document(DOC_ID)
            .set(dades)
    }

    fun recuperar(callback: (EstadistiquesData) -> Unit) {
        db.collection("estadistiques")
            .document(DOC_ID)
            .get()
            .addOnSuccessListener { doc ->
                val dades = if (doc.exists())
                    doc.toObject(EstadistiquesData::class.java) ?: EstadistiquesData()
                else
                    EstadistiquesData()
                callback(dades)
            }
            .addOnFailureListener {
                callback(EstadistiquesData())
            }
    }
}