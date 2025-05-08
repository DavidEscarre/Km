package com.example.km.RecompensaManagment.ui.screeens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.screens.RutaItem
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.core.models.User
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecompensesScreen(
    navController: NavController,
    userState: State<User?>,

) {
    val scrollState = rememberScrollState()
    val user: User? = userState.value

    if (user != null) {
        Log.d("RutaScreen", "fetching ....")
      //  recompe.fetchAllRutas(user.email)
    }
    // Collect the list of rutas from the ViewModel
  //  val rutas by rutaViewModel.rutas.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopBar("Recompenses", userState.value, navController)},
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

        }
        /*
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(rutas) { ruta ->
                RutaItem(ruta, navController, puntGPSViewModel)
            }
        }*/
    }



}
