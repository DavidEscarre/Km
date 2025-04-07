package com.example.km.RutaManagment.data.datasource

import com.example.km.core.models.Ruta
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RutaApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<Ruta>>

    @GET("byId/{rutaId}")
    suspend fun getRutaById(@Path("rutaId") rutaId: Long): Response<Ruta?>

   @POST("create")
   suspend fun createRuta(@Body ruta: Ruta): Response<Long>

  /* @Multipart
   @POST("create")
    suspend fun createRuta(
        @Part("ciclista") ciclista: RequestBody,
        @Part("dataInici") dataInici: RequestBody,
        @Part("dataFinal") dataFinal: RequestBody,
        @Part("puntsGPS") puntsGPS: RequestBody,
    ): Response<Long>*/

    @POST("update")
    suspend fun updateRuta(@Body ruta: Ruta): Response<Long?>
}


