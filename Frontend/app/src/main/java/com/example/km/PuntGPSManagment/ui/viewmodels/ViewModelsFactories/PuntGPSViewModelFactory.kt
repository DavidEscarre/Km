package com.example.km.PuntGPSManagment.ui.viewmodels.ViewModelsFactories

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.SistemaManagment.data.repositories.SistemaRepositoryImpl
import com.example.km.core.models.Sistema
/*

@RequiresApi(Build.VERSION_CODES.O)
class PuntGPSViewModelFactory(
    private val sistema: Sistema,
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PuntGPSViewModel::class.java)) {
            return PuntGPSViewModel(sistema, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}*/