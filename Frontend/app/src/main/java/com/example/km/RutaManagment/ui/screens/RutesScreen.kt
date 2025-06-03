package com.example.km.RutaManagment.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import com.example.km.core.utils.enums.EstatRuta
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar
import java.time.Duration
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RutesScreen(
    rutaViewModel: RutaViewModel,
    puntGPSViewModel: PuntGPSViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    userState: State<User?>

) {
    val user: User? = userState.value

    if (user != null) {
        Log.d("RutaScreen", "fetching ....")
        rutaViewModel.fetchAllRutas(user.email)
        userViewModel.findByEmail(user.email)
    }
    // Collect the list of rutas from the ViewModel
    val rutas by rutaViewModel.rutas.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { TopBar("Rutes", userViewModel.user.collectAsState().value, navController)},
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(rutas.reversed()) { ruta ->
                RutaItem(ruta, navController, puntGPSViewModel)
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RutaItem(ruta: Ruta, navController: NavController, puntGPSViewModel: PuntGPSViewModel) {
    // Calculate duration between start and end (or now)
    val end = ruta.dataFinal ?: LocalDateTime.now()
    val duration = Duration.between(ruta.dataInici, end)
    val hours = duration.toHours()
    val minutes = duration.minusHours(hours).toMinutes()
    val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds
    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A),
            contentColor = Color.White
        ),
        onClick = {

            navController.navigate("ruta_details/${ruta.id}")
        },
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Time, Distance and Balance
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Temps: $timeText", color = Color.White, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Distància: ${"%.2f".format(ruta.distancia)} km",
                        color = Color.White
                    )
                }
                // Balance Card
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (ruta.saldo >= 0) Color(0xFF2ECC71) else Color(0xFFE74C3C),
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {

                        Text(
                            text = "${if (ruta.saldo >= 0) "+" else "-"}${ruta.saldo}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            painter = painterResource(R.drawable.coins),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Velocity info
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Velocitat:", color = Color.White, fontWeight = FontWeight.Bold)
                        Text(
                            "Mitjana: ${"%.1f".format(ruta.velocitatMitjana)} km/h",
                            color = Color.White
                        )
                        Text(
                            "Màxima: ${"%.1f".format(ruta.velocitatMax)} km/h",
                            color = Color.White
                        )
                    }
                    val isPendent = ruta?.estat == EstatRuta.PENDENT
                    val isValidada = ruta.estat == EstatRuta.VALIDA
                    val container = if (isValidada && !isPendent) Color(0xFF2ECC71) else {
                        if(!isValidada && !isPendent) Color(0xFFE74C3C) else Color.Gray
                    }
                    val content = Color.White
                    Button(
                        onClick = { /* Acción si procede */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = container,
                            contentColor = content,
                            disabledContainerColor = container,
                            disabledContentColor = content
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text =  if(isValidada && !isPendent) "Validada" else if(!isValidada && !isPendent) "No validada" else "Pendent",                            color = Color.White
                        )
                    }
                }
            }




        }
    }
}
