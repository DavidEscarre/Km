package com.example.km.RecompensaManagment.ui.screeens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RecompensaViewModel
import com.example.km.core.models.Recompensa
import com.example.km.core.models.User
import com.example.km.core.utils.enums.EstatRecompensa
import com.example.km.core.utils.formatDate
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecompensaDetailScreen(
    recompensaId: Long?,
    recompensaViewModel: RecompensaViewModel,
    userState: State<User?>,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    val user: User? = userState.value

    if (recompensaId != null) {
        recompensaViewModel.findById(recompensaId)
    }


    // Collect the list of rutas from the ViewModel
    val recompensa by recompensaViewModel.recompensa.collectAsState()

    Scaffold(
        topBar = { TopBar("Detalls de Recompensa", userState.value, navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        if (recompensa == null) {
            Text("Recompensa no trobada.")
        }else{
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFfafafa), Color(0xFFFFFF))
                        )
                    )
                    .padding(16.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 90.dp).align(Alignment.Center)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        // Header row
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = recompensa!!.nom,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            SaldoRecompensaCard(Modifier, recompensa!!)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Estat: ",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            EstatRecompensa(recompensa!!.estat)
                        }




                        Spacer(modifier = Modifier.height(12.dp))
                        // Dates section
                        RecompensaDateItem(nom = "Creació", data = recompensa!!.dataCreacio)
                        if(recompensa!!.estat == EstatRecompensa.RESERVADA){
                            recompensa!!.dataReserva?.let { RecompensaDateItem(nom = "Reserva", data = it) }

                        }else if(recompensa!!.estat == EstatRecompensa.ASSIGNADA){
                            recompensa!!.dataReserva?.let { RecompensaDateItem(nom = "Reserva", data = it) }
                            recompensa!!.dataAssignacio?.let {
                                RecompensaDateItem(
                                    nom = "Assignació",
                                    data = it
                                )
                            }
                        }else if(recompensa!!.estat == EstatRecompensa.RECOLLIDA){
                            recompensa!!.dataReserva?.let { RecompensaDateItem(nom = "Reserva", data = it) }
                            recompensa!!.dataAssignacio?.let {
                                RecompensaDateItem(
                                    nom = "Assignació",
                                    data = it
                                )
                            }
                            recompensa!!.dataRecollida?.let {
                                RecompensaDateItem(
                                    nom = "Recollida",
                                    data = it
                                )
                            }
                        }


                        recompensa!!.ciclista?.let {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Usuari",
                                fontSize = 23.sp,
                                color = Color(0xFFFFFFFF)
                            )
                            Text(
                                text = "Nom: ${it.nom}",
                                fontSize = 19.sp,
                                color = Color(0xFFCCCCCC)
                            )
                            Text(
                                text = "Email: ${it.email}",
                                fontSize = 19.sp,
                                color = Color(0xFFCCCCCC)
                            )

                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Punt de bescanvi:",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = recompensa!!.puntBescanvi,
                            fontSize = 17.sp,
                            color = Color(0xFFAAAAAA)
                        )
                    }
                    Row(Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center){
                        ButtonsActionsRecompensa(navController, recompensa!!)
                    }
                }
            }
        }

    }
}
@Composable
fun ButtonsActionsRecompensa(navController: NavController, recompensa: Recompensa){

    if(recompensa.estat == EstatRecompensa.DISPONIBLE){
        Button(
            onClick = {

            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Reservar")

        }
    }else if(recompensa.estat == EstatRecompensa.ASSIGNADA){
        Button(
            onClick = {

            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Recollir")

        }

    }else if(recompensa.estat == EstatRecompensa.RESERVADA){
        Button(
            onClick = {

            },
            modifier = Modifier.padding(12.dp)
        ) {
            Text("Anular Reserva")

        }

    }


}


@Composable
fun EstatRecompensa(estat: EstatRecompensa) {
    val backgroundColor = when (estat) {
        EstatRecompensa.DISPONIBLE -> Color(0xFF4CAF50)
        EstatRecompensa.RESERVADA -> Color(0xFFFFC107)
        EstatRecompensa.ASSIGNADA -> Color(0xFF2196F3)
        EstatRecompensa.RECOLLIDA -> Color(0xFF9E9E9E)
    }
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = estat.name.lowercase().replaceFirstChar { it.uppercase() },
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun SaldoRecompensaCard(modifier: Modifier, recompensa: Recompensa){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =  Color.Black,
            contentColor = if ((recompensa.preu > 0) && recompensa.estat.equals(EstatRecompensa.ASSIGNADA)) Color(0xFFE74C3C)
            else Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {

            Text(
                text = "${if (recompensa.estat.equals(EstatRecompensa.ASSIGNADA)) "-" else ""}${recompensa.preu}",
                color =  if ((recompensa.preu > 0) && recompensa.estat.equals(EstatRecompensa.ASSIGNADA)) Color(0xFFE74C3C)
                else Color.White,
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecompensaDateItem(nom: String, data: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = nom,
            fontSize = 17.sp,
            color = Color(0xFFBBBBBB)
        )

        formatDate(data)?.let {
            Text(
                text = it,
                fontSize = 17.sp,
                color = Color(0xFFEEEEEE)
            )
        }
    }
}
