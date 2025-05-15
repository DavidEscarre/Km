package com.example.km.RecompensaManagment.data.datasource

import com.example.km.core.models.Recompensa
import com.example.km.core.models.Ruta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecompensaApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<Recompensa>>


    @GET("byCiclistaEmail/{ciclistaEmail}")
    suspend fun getAllByCiclistaEmail(@Path("ciclistaEmail") ciclistaEmail: String): Response<List<Recompensa>>


    @GET("byId/{recompensaId}")
    suspend fun getRecompensaById(@Path("recompensaId") recompensaId: Long): Response<Recompensa?>

    @POST("reservar/{recompensaId}")
    @Headers("Accept: text/plain")
    suspend fun reservarRecompensa(@Path("recompensaId") recompensaId: Long, @Query("email") email: String) :Response<String>

    @POST("anularReserva/{recompensaId}")
    @Headers("Accept: text/plain")
    suspend fun anularReservaRecompensa(@Path("recompensaId") recompensaId: Long, @Query("email") email: String) :Response<String>

    @POST("recollir/{recompensaId}")
    suspend fun recollirRecompensa(@Path("recompensaId") recompensaId: Long, @Query("email") email: String) :Response<String>


}


