package com.example.km.RutaManagment.domain.repositories

import com.example.km.core.models.Recompensa
import com.example.km.core.models.Ruta
import retrofit2.Response

interface RecompensaRepository {
    suspend fun findAll(): Response<List<Recompensa>>

    suspend fun getRecompensaById(recompensaId: Long): Response<Recompensa?>

    suspend fun getAllByCiclistaEmail(ciclistaEmail: String): Response<List<Recompensa>>

    suspend fun reservarRecompensa(recompensaId: Long, email: String): Response<String>

    suspend fun anularReservaRecompensa(recompensaId: Long, email: String): Response<String>





}

