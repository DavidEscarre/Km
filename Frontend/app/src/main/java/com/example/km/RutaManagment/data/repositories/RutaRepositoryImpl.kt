package com.example.km.RutaManagment.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.PuntGPSManagment.data.datasource.PuntGPSApiRest
import com.example.km.PuntGPSManagment.data.datasource.PuntGPSRetrofitInstance
import com.example.km.PuntGPSManagment.domain.repositories.PuntGPSRepository
import com.example.km.RutaManagment.data.datasource.RutaApiRest
import com.example.km.RutaManagment.data.datasource.RutaRetrofitInstance
import com.example.km.RutaManagment.domain.repositories.RutaRepository
import com.example.km.UserManagment.data.datasource.AuthApiRest
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.UserManagment.domain.repositories.LoginRepository
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.create
import retrofit2.http.Part
@RequiresApi(Build.VERSION_CODES.O)
class RutaRepositoryImpl: RutaRepository {


    val rutaApiRest: RutaApiRest =  RutaRetrofitInstance.retrofitInstance.create(RutaApiRest::class.java)

    //val rutaApiRest = RutaRetrofitInstance.retrofitInstance.create()

    override suspend fun findAll(): Response<List<Ruta>> {
        val response = rutaApiRest.findAll()
        return response
    }

    override suspend fun getRutaById(rutaId: Long): Response<Ruta?> {
        return  rutaApiRest.getRutaById(rutaId)
       // return response
    }

 /*  override suspend fun createRuta(ciclista: RequestBody,dataInici: RequestBody, dataFinal: RequestBody,puntsGPS: RequestBody): Response<Long> {
        return rutaApiRest.createRuta(ciclista, dataInici, dataFinal, puntsGPS)
       // return response
    }*/

     override suspend fun createRuta(ruta: Ruta): Response<Long> {
       return rutaApiRest.createRuta(ruta)

   }

    override suspend fun updateRuta(ruta: Ruta): Response<Long?> {
        val response = rutaApiRest.updateRuta(ruta)
        return response
    }

}