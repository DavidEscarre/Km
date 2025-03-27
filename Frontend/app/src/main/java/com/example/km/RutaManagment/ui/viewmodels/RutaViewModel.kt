package com.example.km.RutaManagment.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.RutaManagment.data.datasource.RutaApiRest
import com.example.km.RutaManagment.data.datasource.RutaRetrofitInstance
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RutaViewModel: ViewModel() {

    private val RutaApi: RutaApiRest = RutaRetrofitInstance.retrofitInstance.create(RutaApiRest::class.java)

    private val _ruta = MutableStateFlow<Ruta?>(null)
    val ruta: StateFlow<Ruta?>  = _ruta

    private val _rutaIniciada = MutableStateFlow<Ruta?>(null)
    val rutaIniciada: StateFlow<Ruta?>  = _rutaIniciada

   /* private val _puntsGPSRuta = MutableStateFlow<List<PuntGPS>>(emptyList())
    val puntsGPSRuta: StateFlow<Ruta?> get() = _puntsGPSRuta*/

    fun iniciarRuta(ruta: Ruta,context: android.content.Context, onSuccess: () -> Unit, onError: (String) -> Unit) {

        viewModelScope.launch {
            try {

                val response = RutaApi.createRuta(ruta)

                if (response.isSuccessful) {

                    Log.d("Ruta", "✅ Ruta iniciada")
                    onSuccess()
                    _rutaIniciada.value = ruta
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

                val response = RutaApi.updateRuta(ruta)

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