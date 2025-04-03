package com.example.km.PuntGPSManagment.domain.repositories

import com.example.km.core.models.PuntGPS
import com.example.km.core.models.User
import retrofit2.Response
import retrofit2.http.Path

interface PuntGPSRepository {
    suspend fun findAll(): Response<List<PuntGPS>>

    suspend fun getPuntGPSById(puntGPSId: Long): Response<PuntGPS>

    suspend fun createPuntGPS(puntGPS: PuntGPS): Response<Long>
}