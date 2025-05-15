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

    private val _recompensasUser = MutableStateFlow<List<Recompensa>>(emptyList<Recompensa>())
    val recompensasUser: StateFlow<List<Recompensa>>  = _recompensasUser



    private val _recompensa = MutableStateFlow<Recompensa?>(null)
    val recompensa: StateFlow<Recompensa?>  = _recompensa

    private val _recompensaError = MutableStateFlow<String?>(null)
    val recompensaError: StateFlow<String?>  = _recompensaError

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
    fun fetchAllUserRecompensas(ciclistaEmail: String){
        viewModelScope.launch {
            try{
                val response = recompensaRepo.getAllByCiclistaEmail(ciclistaEmail)
                if(response.isSuccessful){
                    Log.d("RutaById", "Las Recompensas san encontrao coñooo")
                    _recompensasUser.value = response.body()!!
                }else{
                    Log.e("RutaById", "Las Recompensas no san pogut trobar")
                    _recompensasUser.value = emptyList()
                }
            }catch(e: Exception){
                _recompensasUser.value = emptyList()
                e.printStackTrace()
            }
        }
    }

    fun reservarRecompensa(id: Long, email: String){
        viewModelScope.launch {
            try{
                val response = recompensaRepo.reservarRecompensa(id,email)
                if(response.isSuccessful){
                    Log.d("RecompensaById", "La recompensa sareservado  coñooo")
                    findById(id)
                    _recompensaError.value = null
                }else{
                    val errorMessage = response.errorBody()?.string() ?: "Error desconegut"
                    _recompensaError.value = errorMessage.toString()
                    Log.e("RecompensaById", "La recompensa no sa pogut reservar: $errorMessage")

                    Log.e("RecompensaById", "La recompensa no sa pogut reservar")
                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun anularReservaRecompensa(id: Long, email: String){
        viewModelScope.launch {
            try{
                val response = recompensaRepo.anularReservaRecompensa(id,email)
                if(response.isSuccessful){
                    Log.d("RecompensaById", "La reserva de la recompensa ha sido anulada  coñooo")
                    findById(id)
                    _recompensaError.value = null
                }else{
                    _recompensaError.value = response.body()
                    Log.e("RecompensaById", "La reserva de la recompensa no sa podido anulada coñooo")
                }
            }catch(e: Exception){
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
                    _recompensaError.value = null
                    _recompensa.value = response.body()
                }else{
                    Log.e("RecompensaById", "La recompensa no sa pogut trobar")
                    _recompensa.value = null
                    _recompensaError.value = null
                }
            }catch(e: Exception){
                _recompensa.value = null
                _recompensaError.value = null
                e.printStackTrace()
            }
        }
    }

    fun resetFetchedRecompensa(){
        viewModelScope.launch {
            _recompensa.value = null
        }
    }

}