package com.example.km.RutaManagment.data.repositories

import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.km.PuntGPSManagment.data.datasource.PuntGPSApiRest
import com.example.km.PuntGPSManagment.data.datasource.PuntGPSRetrofitInstance
import com.example.km.PuntGPSManagment.domain.repositories.PuntGPSRepository
import com.example.km.RutaManagment.data.datasource.RutaApiRest
import com.example.km.RutaManagment.data.datasource.RutaRetrofitInstance
import com.example.km.RutaManagment.domain.repositories.RutaRepository
import com.example.km.SistemaManagment.data.repositories.SistemaRepositoryImpl
import com.example.km.UserManagment.data.datasource.AuthApiRest
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.UserManagment.domain.repositories.LoginRepository
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.example.km.core.models.Sistema
import com.example.km.core.models.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.create
import retrofit2.http.Part
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class RutaRepositoryImpl: RutaRepository {
    private val sistemaRepo: SistemaRepositoryImpl = SistemaRepositoryImpl()

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
        val sistema = sistemaRepo.getSistemaById(1).body()



        /*var puntsKm = 1.0
        if(sistemaRepo.sistema.value != null){
            puntsKm = sistemaRepo.sistema.value!!.puntsKm
        }*/

        Log.d("SistemaRepoLLA", "Sistema trobat i carregat correctament puntsKm ${sistema?.puntsKm}")

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
        val saldo: Double = distancia * (sistema?.puntsKm ?: 1.00 )


        ruta.saldo = BigDecimal(saldo).setScale(2, RoundingMode.HALF_UP).toDouble()
        ruta.distancia = BigDecimal(distancia).setScale(2, RoundingMode.HALF_UP).toDouble()
        ruta.velocitatMax = BigDecimal(velMax).setScale(2, RoundingMode.HALF_UP).toDouble()
        ruta.velocitatMitjana = BigDecimal(velMig).setScale(2, RoundingMode.HALF_UP).toDouble()


        val response = rutaApiRest.updateRuta(ruta)
        return response
    }

}