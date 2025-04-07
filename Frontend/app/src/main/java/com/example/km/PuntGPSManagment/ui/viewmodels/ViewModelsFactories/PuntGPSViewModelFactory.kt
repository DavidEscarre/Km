/*package com.example.km.PuntGPSManagment.ui.viewmodels.ViewModelsFactories

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
@RequiresApi(Build.VERSION_CODES.O)
class PuntGPSViewModelFactory(
    private val rutaViewModel: RutaViewModel,
    private val application: Application
) : ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PuntGPSViewModel(rutaViewModel, application) as T
    }
}*/