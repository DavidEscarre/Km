package com.example.km.SistemaManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.SistemaManagment.data.repositories.SistemaRepositoryImpl
import com.example.km.core.models.Sistema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class SistemaViewModel(
    private val sistemaRepo: SistemaRepositoryImpl = SistemaRepositoryImpl()
): ViewModel()  {

    // Exponemos el flujo del sistema
    val sistemaFlow = sistemaRepo.sistema

    //private val sistemaRepo = SistemaRepositoryImpl()

    private val _puntsKm = MutableStateFlow<Double>(1.00)
    val puntsKm: StateFlow<Double> get() = _puntsKm

    private val _velMaxValida = MutableStateFlow<Double>(60.00)
    val velMaxValida: StateFlow<Double> get() = _velMaxValida

    private val _tempsMaxAtur = MutableStateFlow<Long>(300000L)
    val tempsMaxAtur: StateFlow<Long> get() = _tempsMaxAtur

    private val _tempsMaxRec = MutableStateFlow<Long>(259200000L)
    val tempsMaxRec: StateFlow<Long> get() = _tempsMaxRec

    private val _precisioPunts = MutableStateFlow<Long>(2000L)
    val precisioPunts: StateFlow<Long> get() = _precisioPunts

    private val _sistema = MutableStateFlow<Sistema?>(null)
    val sistema: StateFlow<Sistema?> get() = _sistema


    fun findById(sistemaId: Long){

        viewModelScope.launch {
            val response =sistemaRepo.getSistemaById(sistemaId)
            if(response.isSuccessful){
                Log.d("SistemaVM", "Sistema trobat ${sistemaId} i carregat correctament")
                _puntsKm.value = response.body()?.puntsKm ?: 1.00
                _velMaxValida.value = response.body()?.velMaxValida ?: 60.00
                _tempsMaxAtur.value = response.body()?.tempsMaxAtur ?: 300000L
                _tempsMaxRec.value = response.body()?.tempsMaxRec ?: 259200000L
                _precisioPunts.value = response.body()?.precisioPunts ?: 2000L
                _sistema.value = response.body()
            }else{
                Log.e("SistemaVM", "Sistema no trobat ${sistemaId}")

            }


        }
    }
    fun findFirst(){

        viewModelScope.launch {
            val response =sistemaRepo.findFirst()

            if(response.isSuccessful){
                Log.d("SistemaVM", "Sistema trobat i carregat correctament")
                _puntsKm.value = response.body()?.first()?.puntsKm ?: 1.00
                _velMaxValida.value = response.body()?.first()?.velMaxValida ?: 60.00
                _tempsMaxAtur.value = response.body()?.first()?.tempsMaxAtur ?: 300000L
                _tempsMaxRec.value = response.body()?.first()?.tempsMaxRec ?: 259200000L
                _precisioPunts.value = response.body()?.first()?.precisioPunts ?: 2000L

            }else{
                Log.e("SistemaVM", "Sistema no trobat")

            }


        }
    }

}