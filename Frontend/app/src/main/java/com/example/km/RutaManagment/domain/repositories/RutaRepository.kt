package com.example.km.RutaManagment.domain.repositories

import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

interface RutaRepository {
    suspend fun findAll(): Response<List<Ruta>>

    suspend fun getRutaById(rutaId: Long): Response<Ruta?>

    //suspend fun createRuta(ciclista: RequestBody, dataInici: RequestBody, dataFinal: RequestBody, puntsGPS: RequestBody): Response<Long>
    suspend fun createRuta(ruta: Ruta): Response<Long>

    suspend fun updateRuta(ruta: Ruta): Response<Long?>
}