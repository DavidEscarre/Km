package com.example.km.RutaManagment.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.RecompensaManagment.data.datasource.RecompensaApiRest
import com.example.km.RecompensaManagment.data.datasource.RecompensaRetrofitInstance
import com.example.km.RutaManagment.domain.repositories.RecompensaRepository
import com.example.km.SistemaManagment.data.repositories.SistemaRepositoryImpl
import com.example.km.core.models.Recompensa
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class RecompensaRepositoryImpl: RecompensaRepository {
    private val sistemaRepo: SistemaRepositoryImpl = SistemaRepositoryImpl()

    val recompensaApiRest: RecompensaApiRest =  RecompensaRetrofitInstance.retrofitInstance.create(RecompensaApiRest::class.java)
    override suspend fun findAll(): Response<List<Recompensa>> {
        val response = recompensaApiRest.findAll()
        return response
    }

    override suspend fun getRecompensaById(recompensaId: Long): Response<Recompensa?> {
        val response = recompensaApiRest.getRecompensaById(recompensaId)
        return response
    }

    override suspend fun getAllByCiclistaEmail(ciclistaEmail: String): Response<List<Recompensa>> {
        val response = recompensaApiRest.getAllByCiclistaEmail(ciclistaEmail)
        return response
    }

    override suspend fun reservarRecompensa(recompensaId: Long, email: String): Response<String> {
        val response = recompensaApiRest.reservarRecompensa(recompensaId, email)
        return response
    }
    override suspend fun anularReservaRecompensa(recompensaId: Long, email: String): Response<String> {
        val response = recompensaApiRest.anularReservaRecompensa(recompensaId, email)
        return response
    }

    override suspend fun recollirRecompensa(recompensaId: Long, email: String): Response<String> {
        val response = recompensaApiRest.recollirRecompensa(recompensaId, email)
        return response
    }



}