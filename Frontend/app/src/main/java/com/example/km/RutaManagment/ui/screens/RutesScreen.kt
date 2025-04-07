package com.example.km.RutaManagment.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.User
import com.example.km.navigation.BottomNavigationBar


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RutesScreen(rutaViewModel: RutaViewModel, puntGPSViewModel: PuntGPSViewModel, navController: NavController, userState: State<User?>) {
    val scrollState = rememberScrollState()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(scrollState)// Habilita el scroll
                .padding(paddingValues)
        ) {

        }
        }



}
