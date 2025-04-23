package com.example.km.core.utils

import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.km.SistemaManagment.data.repositories.SistemaRepositoryImpl
import com.example.km.SistemaManagment.ui.viewmodels.SistemaViewModel
import com.example.km.core.models.Sistema
import kotlinx.coroutines.CoroutineStart

fun base64ToByteArray(imageStr: String): ByteArray?{
    try {
        if (imageStr.isNullOrEmpty()) return null
        val imageBytes = Base64.decode(imageStr, Base64.DEFAULT)
        return imageBytes
    }catch (e: Exception){
        return null
    }
}


// IP POR DEFECTO DE LA MAQUINA VIRTUAL
const val IP_VM = "10.0.2.2"

// IP DE MI PORTATIL CONECTADO AL PUNTO DE ACCESO DE DATOS DE MI MOVIL
const val IP_PC = "192.168.67.227"
const val IP_PCWifiCasa = "192.168.1.107"

// URL DE CONEXION DE LA APP
const val URL_IP_APP = IP_VM

var sistema: Sistema? = null
/*
@RequiresApi(Build.VERSION_CODES.O)
suspend fun sistemaLoader(sistemaViewModel: SistemaViewModel):Sistema? {

    sistemaViewModel.findFirst()
    sistema = sistemaViewModel.sistema.value
    Log.d("sistemaMethod", "sistema: $sistema, precision: ${sistema?.precicioPunts}")
    return  sistemaViewModel.sistema.value

}
fun getActualSistema(): Sistema?{
    return sistema
}*/