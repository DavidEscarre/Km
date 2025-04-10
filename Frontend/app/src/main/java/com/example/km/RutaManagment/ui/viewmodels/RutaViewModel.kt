package com.example.km.RutaManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.PuntGPSManagment.data.repositories.PuntGPSRepositoryImpl
import com.example.km.RutaManagment.data.datasource.RutaApiRest
import com.example.km.RutaManagment.data.datasource.RutaRetrofitInstance
import com.example.km.RutaManagment.data.repositories.RutaRepositoryImpl
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class RutaViewModel: ViewModel() {

    private val puntGPSRepo = PuntGPSRepositoryImpl()

    private val rutaRepo = RutaRepositoryImpl()

    private val _rutaAct = MutableStateFlow<Ruta?>(null)
    val rutaAct: StateFlow<Ruta?>  = _rutaAct

    private val _rutaIniciada = MutableStateFlow<Ruta?>(null)
    val rutaIniciada: StateFlow<Ruta?>  = _rutaIniciada

   private val _puntsGPSRuta = MutableStateFlow<List<PuntGPS>>(emptyList())
    val puntsGPSRuta: MutableStateFlow<List<PuntGPS>> get() = _puntsGPSRuta


    fun findById(id: Long): Ruta?{
        var res: Ruta? = null
        viewModelScope.launch {

           try{
              val response = rutaRepo.getRutaById(id)
               res= response.body()
               if(response.isSuccessful){
                   Log.d("RutaById", "La ruta sa encontrao coñooo")
                   _rutaAct.value = response.body()
               }else{
                   Log.e("RutaById", "La ruta no sa pogut trobar")
                   _rutaAct.value = null
               }

           }catch(e: Exception){
               _rutaAct.value = null
               e.printStackTrace()


           }
       }

        return res
    }

    //fun iniciarRuta(ciclista: User, dataInici:String, dataFinal:String, puntsGPS: List<PuntGPS>,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
    fun iniciarRuta(ruta: Ruta,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
        vuidarPuntsGPSList()
        viewModelScope.launch {
            try {


/*
                val mediaType = "application/json".toMediaTypeOrNull()

                val ciclistaJson = Gson().toJson(ciclista).toRequestBody(mediaType)
                val dataIniciBody = dataInici.toRequestBody(mediaType)
                val dataFinalBody = dataFinal.toRequestBody(mediaType)
                val puntsJson = Gson().toJson(puntsGPS).toRequestBody(mediaType)
*/
                //val response = rutaRepo.createRuta(ciclistaJson,dataIniciBody, dataFinalBody, puntsJson)
                val response = rutaRepo.createRuta(ruta)
                response.body()?.let { findById(it) }
                if (response.isSuccessful) {
                  //  response.body()?.let { findById(it) }
                    Log.d("Ruta", "✅ Ruta iniciada")
                    onSuccess()

                    _rutaAct.value = findById(response.body()!!)
                    _rutaIniciada.value = rutaAct.value
                    Log.d("Rutasadadsdas", "✅ ${rutaAct.value} ")
                } else {

                    val errorBody = response.errorBody()?.string()
                    Log.e("Ruta", "❌ Error al iniciar la ruta ${response.code()} - ${errorBody.toString()}")
                    val errorMessage =
                        "❌ ${response.code()} - ${errorBody.toString()}"
                    onError(errorMessage)
                    _rutaIniciada.value = null
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

        }

    }
    fun aturarRuta(ruta: Ruta, puntsGPSList: List<PuntGPS> ,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch {
            try {
                CrearPuntsGPSRuta(puntsGPSList)
                val locationList = puntGPSRepo.getPuntGPSByIdRuta(ruta.id)
                Log.d(" Raaaaaaaaaaaaaaaaaaaa111", locationList.body()!!.size.toString())
                ruta.puntsGPS = locationList.body()



                Log.d(" RUTAEERRRccQQ1111",  ruta.puntsGPS.size.toString())
                val response = rutaRepo.updateRuta(ruta)

                if (response.isSuccessful) {

                    Log.d("Ruta", "✅ Ruta aturada")
                    onSuccess()
                    _rutaIniciada.value = null
                } else {
                    Log.e("Ruta", "❌ Error al aturar la ruta")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)

                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }
    suspend fun CrearPuntsGPSRuta(puntsGPSList: List<PuntGPS>) {
        Log.d("PuntGPS RUTAsssss", puntsGPSList.size.toString())

        for (it in puntsGPSList) {
            try {
                it.ruta = rutaAct.value
                Log.d("PuntGPS RUTA", "${rutaAct.value}, Punt = ${it.ruta.id}")
                val response = puntGPSRepo.createPuntGPS(it)
                if (response.isSuccessful) {
                    Log.d("PuntGPS RUTA", "PUNT CREAT: ${response.body()}")
                    _puntsGPSRuta.value = _puntsGPSRuta.value + puntGPSRepo.getPuntGPSById(response.body()!!).body()!!
                } else {
                    Log.e("PuntGPS RUTA", "❌ Error al crear el puntGPS")
                }
            } catch (e: Exception) {
                Log.e("PuntGPS RUTA", "❌ Exception al crear el puntGPS: ${e.message}")
            }
        }
    }

    fun vuidarPuntsGPSList(){
        _puntsGPSRuta.value = emptyList()
    }
}