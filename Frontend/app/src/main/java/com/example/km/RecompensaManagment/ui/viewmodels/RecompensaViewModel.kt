package com.example.km.RutaManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.RutaManagment.data.repositories.RecompensaRepositoryImpl
import com.example.km.RutaManagment.data.repositories.RutaRepositoryImpl
import com.example.km.core.models.PuntGPS
import com.example.km.core.models.Recompensa
import com.example.km.core.models.Ruta
import com.example.km.core.utils.enums.EstatRuta
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class RecompensaViewModel(): ViewModel() {

    private val _recompesas = MutableStateFlow<List<Recompensa>>(emptyList<Recompensa>())
    val recompesas: StateFlow<List<Recompensa>>  = _recompesas

    private val _recompesa = MutableStateFlow<Recompensa?>(null)
    val recompesa: StateFlow<Recompensa?>  = _recompesa



    private val recompensaRepo = RecompensaRepositoryImpl()


/*
    fun fetchAllRecompensas(ciclistaEmail: String){
        viewModelScope.launch {
            try{
                val response = recompensaRepo.getRecompensaById(ciclistaEmail)

                if(response.isSuccessful){
                    Log.d("RutaById", "La ruta sa encontrao coñooo")
                    _recompesas.value = response.body()!!
                }else{
                    Log.e("RutaById", "La ruta no sa pogut trobar")
                    _recompesas.value = emptyList()
                }

            }catch(e: Exception){
                _recompesas.value = emptyList()
                e.printStackTrace()


            }
        }
    }*/
    fun findById(id: Long){
        viewModelScope.launch {

            try{
                val response = recompensaRepo.getRecompensaById(id)
                if(response.isSuccessful){
                    Log.d("RecompensaById", "La recompensa sa encontrao coñooo")

                    _recompesa.value = response.body()

                }else{
                    Log.e("RecompensaById", "La recompensa no sa pogut trobar")
                    _recompesa.value = null
                }

            }catch(e: Exception){
                _recompesa.value = null
                e.printStackTrace()


            }
        }
    }

}