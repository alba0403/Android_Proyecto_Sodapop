package com.example.sodapop.services

import com.example.sodapop.services.NuevaRecetaRequest
import com.example.sodapop.model.Receta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecetasService {
    @GET("api/recetas")
    suspend fun llistaReceptes(): Response<List<Receta>>

    @DELETE("api/recetas/{id}")
    suspend fun eliminarRecepta(
        @Path("id") id: Long
    ): Response<Unit>

    @POST("api/recetas")
    suspend fun crearRecepta(
        @Body receta: NuevaRecetaRequest
    ): Response<Receta>
}