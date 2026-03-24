package com.example.sodapop

import retrofit2.Response
import retrofit2.http.*

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