package com.example.km.SistemaManagment.domain.repositories

import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.example.km.core.models.Sistema
import com.example.km.core.models.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

interface SistemaRepository {
    suspend fun findAll(): Response<List<Sistema>>
    suspend fun findFirst(): Response<List<Sistema>>
    suspend fun getSistemaById(sistemaId: Long): Response<Sistema?>


}