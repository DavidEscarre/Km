package com.example.km.RecompensaManagment.data.datasource

import com.example.km.core.models.Recompensa
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecompensaApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<Recompensa>>

    @GET("byId/{recompensaId}")
    suspend fun getRecompensaById(@Path("recompensaId") recompensaId: Long): Response<Recompensa>
}


