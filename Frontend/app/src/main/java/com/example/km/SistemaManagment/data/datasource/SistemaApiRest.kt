package com.example.km.SistemaManagment.data.datasource

import com.example.km.core.models.Sistema
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SistemaApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<Sistema>>

    @GET("byId/{sistemaId}")
    suspend fun getSistemaById(@Path("sistemaId") sistemaId: Long): Response<Sistema>
}


