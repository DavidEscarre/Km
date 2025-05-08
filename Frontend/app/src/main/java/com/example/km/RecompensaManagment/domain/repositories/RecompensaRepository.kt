package com.example.km.RutaManagment.domain.repositories

import com.example.km.core.models.Recompensa
import retrofit2.Response

interface RecompensaRepository {
    suspend fun findAll(): Response<List<Recompensa>>

    suspend fun getRecompensaById(recompensaId: Long): Response<Recompensa?>



    //suspend fun createRuta(ciclista: RequestBody, dataInici: RequestBody, dataFinal: RequestBody, puntsGPS: RequestBody): Response<Long>

     suspend fun getAllByCiclistaEmail(ciclistaEmail: String): Response<List<Recompensa>>


}

