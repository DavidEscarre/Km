package com.example.km.RutaManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.RutaManagment.data.repositories.RecompensaRepositoryImpl
import com.example.km.core.models.Recompensa
import com.example.km.core.models.User
import com.example.km.core.utils.enums.EstatRecompensa
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class RecompensaViewModel(): ViewModel() {

    private val _recompensas = MutableStateFlow<List<Recompensa>>(emptyList<Recompensa>())
    val recompensas: StateFlow<List<Recompensa>>  = _recompensas

    private val _recompensa = MutableStateFlow<Recompensa?>(null)
    val recompensa: StateFlow<Recompensa?>  = _recompensa

    private val recompensaRepo = RecompensaRepositoryImpl()


    fun fetchAllRecompensas(ciclistaEmail: String){
        viewModelScope.launch {
            try{
                val response = recompensaRepo.getAllByCiclistaEmail(ciclistaEmail)

                if(response.isSuccessful){
                    Log.d("RutaById", "Las Recompensas san encontrao coñooo")

                    val responseALL = recompensaRepo.findAll()
                    var recompensesDisponibles = responseALL.body()?.filter { it.estat == EstatRecompensa.DISPONIBLE  }
                    _recompensas.value = response.body()!!.union(recompensesDisponibles!!).toList()

                }else{
                    Log.e("RutaById", "Las Recompensas no san pogut trobar")
                    _recompensas.value = emptyList()
                }

            }catch(e: Exception){
                _recompensas.value = emptyList()
                e.printStackTrace()


            }
        }
    }
    fun findById(id: Long){
        viewModelScope.launch {

            try{
                val response = recompensaRepo.getRecompensaById(id)
                if(response.isSuccessful){
                    Log.d("RecompensaById", "La recompensa sa encontrao coñooo")

                    _recompensa.value = response.body()

                }else{
                    Log.e("RecompensaById", "La recompensa no sa pogut trobar")
                    _recompensa.value = null
                }

            }catch(e: Exception){
                _recompensa.value = null
                e.printStackTrace()


            }
        }
    }

}