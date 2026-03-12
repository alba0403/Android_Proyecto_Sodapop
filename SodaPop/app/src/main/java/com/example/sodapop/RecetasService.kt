package com.example.sodapop

import retrofit2.Response
import retrofit2.http.*

interface RecetasService {
    @GET("receptes/")
    suspend fun llistaReceptes(): Response<List<Receta>>

    @DELETE("receptes/{id}")
    suspend fun eliminarRecepta(
        @Path("id") id: Long
    ): Response<Unit>

    @POST("receptes/")
    suspend fun crearRecepta(
        @Body receta: NuevaRecetaRequest
    ): Response<Receta>
}