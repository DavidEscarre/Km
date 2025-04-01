package com.example.km.PuntGPSManagment.data.datasource

import com.example.km.core.models.PuntGPS
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PuntGPSApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<PuntGPS>>

    @GET("byId/{puntGPSId}")
    suspend fun getPuntGPSById(@Path("puntGPSId") puntGPSId: Long): Response<PuntGPS>

    @POST("create")
    suspend fun createPuntGPS(@Body puntGPS: PuntGPS): Response<Long>
}


