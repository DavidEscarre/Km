package com.example.km.RutaManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import okhttp3.Response
@RequiresApi(Build.VERSION_CODES.O)
class RutaViewModel: ViewModel() {


    private val rutaRepo = RutaRepositoryImpl()

    private val _rutaAct = MutableStateFlow<Ruta?>(null)
    val rutaAct: StateFlow<Ruta?>  = _rutaAct

    private val _rutaIniciada = MutableStateFlow<Ruta?>(null)
    val rutaIniciada: StateFlow<Ruta?>  = _rutaIniciada

   /* private val _puntsGPSRuta = MutableStateFlow<List<PuntGPS>>(emptyList())
    val puntsGPSRuta: StateFlow<Ruta?> get() = _puntsGPSRuta*/


    fun findById(id: Long){
       viewModelScope.launch {
           try{
              val response = rutaRepo.getRutaById(id)
               if(response.isSuccessful){
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
    }

    //fun iniciarRuta(ciclista: User, dataInici:String, dataFinal:String, puntsGPS: List<PuntGPS>,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {
    fun iniciarRuta(ruta: Ruta,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {

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

                if (response.isSuccessful) {

                    Log.d("Ruta", "✅ Ruta iniciada")
                    onSuccess()
                    findById(response.body()!!)
                    _rutaIniciada.value = rutaAct.value
                } else {
                    Log.e("Ruta", "❌ Error al iniciar la ruta")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)
                    _rutaIniciada.value = null
                }
            }catch(e: Exception){
                e.printStackTrace()
            }

        }

    }
    fun aturarRuta(ruta: Ruta,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch {
            try {

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
}