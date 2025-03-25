package com.example.km.RutaManagment.data.datasource

import com.example.km.core.models.Ruta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RutaApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<Ruta>>

    @GET("byId/{rutaId}")
    suspend fun getRutaById(@Path("rutaId") rutaId: Long): Response<Ruta>
}


