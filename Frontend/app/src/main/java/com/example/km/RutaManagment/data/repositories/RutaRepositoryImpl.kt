package com.example.km.RutaManagment.data.repositories

import android.location.Location
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
import java.time.ZoneId

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



     override suspend fun createRuta(ruta: Ruta): Response<Long> {
       return rutaApiRest.createRuta(ruta)

   }

    override suspend fun updateRuta(ruta: Ruta): Response<Long?> {

        val zoneId = ZoneId.systemDefault()

        var distancia = 0.00
        var velMax = 0.00


        for(i in 0 until ruta.puntsGPS.size - 1){

            val a = ruta.puntsGPS[i]
            val b = ruta.puntsGPS[i+1]

            if(a != null && b != null){
                val results = FloatArray(1)
                Location.distanceBetween(
                    a.latitud,
                    a.longitud,
                    b.latitud,
                    b.longitud,
                    results
                )

                distancia += results[0].toDouble()


                val ATimeMilis =  a.marcaTemps.atZone(zoneId).toInstant().toEpochMilli()
                val BTimeMilis =  b.marcaTemps.atZone(zoneId).toInstant().toEpochMilli()

                val ABTimeSeg: Double = (BTimeMilis - ATimeMilis).toDouble() / 1000
                val ABvel = (results[0].toDouble() / ABTimeSeg)*3.6


                if(ABvel > velMax){
                    velMax = ABvel
                }
            }


        }


        val StartMilis =  ruta.dataInici.atZone(zoneId).toInstant().toEpochMilli()
        val EndMilis = ruta.dataFinal.atZone(zoneId).toInstant().toEpochMilli()

        val durada: Double = (EndMilis - StartMilis).toDouble() / 1000
        val velMig  = (distancia / durada) *3.6
        distancia /= 1000
        val saldo: Double = distancia * 1


        ruta.saldo = saldo
        ruta.distancia = distancia
        ruta.velocitatMax = velMax
        ruta.velocitatMitjana = velMig


        val response = rutaApiRest.updateRuta(ruta)
        return response
    }

}