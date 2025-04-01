package com.example.km.PuntGPSManagment.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.PuntGPSManagment.data.datasource.PuntGPSApiRest
import com.example.km.PuntGPSManagment.data.datasource.PuntGPSRetrofitInstance
import com.example.km.PuntGPSManagment.domain.repositories.PuntGPSRepository
import com.example.km.UserManagment.data.datasource.AuthApiRest
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.UserManagment.domain.repositories.LoginRepository
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.User
import retrofit2.Response
@RequiresApi(Build.VERSION_CODES.O)
class PuntGPSRepositoryImpl: PuntGPSRepository {


    val puntGPSApiRest: PuntGPSApiRest=  PuntGPSRetrofitInstance.retrofitInstance.create(PuntGPSApiRest::class.java)

    override suspend fun findAll(): Response<List<PuntGPS>> {
        val response = puntGPSApiRest.findAll()
        return response
    }

    override suspend fun getPuntGPSById(puntGPSId: Long): Response<PuntGPS> {
        val response = puntGPSApiRest.getPuntGPSById(puntGPSId)
        return response
    }

    override suspend fun createPuntGPS(puntGPS: PuntGPS): Response<Long> {
        val response = puntGPSApiRest.createPuntGPS(puntGPS)
        return response
    }

}