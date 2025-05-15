package com.example.km.RecompensaManagment.ui.screeens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RecompensaViewModel
import com.example.km.core.models.Recompensa
import com.example.km.core.models.User
import com.example.km.core.ui.theme.errorMessageColor
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
    var showDialog by remember { mutableStateOf(false) }

    var showEntregatImage by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    LaunchedEffect(recompensaId) {
        recompensaId?.let { recompensaViewModel.findById(it) }
    }

    val recompensa by recompensaViewModel.recompensa.collectAsState()
    val recompensaError by recompensaViewModel.recompensaError.collectAsState()
    if(recompensa?.estat==EstatRecompensa.RECOLLIDA){
        showEntregatImage = true
    }
    Scaffold(
        topBar = { TopBar("Detalls de Recompensa", userState.value, navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFfafafa), Color(0xFFFFFF))
                    )
                )
                .padding(16.dp)
        ) {
            if (recompensa == null) {
                Text("Recompensa no trobada.")
            } else {


                // Contingut principal
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {

                        // Pantalla ENTREGAT
                        if (showEntregatImage) {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource( R.drawable.success),
                                    contentDescription = "ENTREGAT EXITOSAMENT",
                                    tint = Color.Green
                                )
                                Text(
                                    text = "ENTREGAT",
                                    color = Color.White,
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Center
                                )


                            }

                        }
                        // Encapçalament
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
                        // Estat
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Estat:",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            EstatRecompensa(recompensa!!.estat)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        // Dates
                        RecompensaDateItem(nom = "Creació", data = recompensa!!.dataCreacio)
                        recompensa!!.dataReserva?.let { RecompensaDateItem(nom = "Reserva", data = it) }
                        recompensa!!.dataAssignacio?.let { RecompensaDateItem(nom = "Assignació", data = it) }
                        recompensa!!.dataRecollida?.let { RecompensaDateItem(nom = "Recollida", data = it) }
                        // Ciclista
                        recompensa!!.ciclista?.let {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Usuari",
                                fontSize = 23.sp,
                                color = Color.White
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
                        // Punt de bescanvi
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
                        Spacer(modifier = Modifier.height(16.dp))
                        // Descripció i observacions
                        Text(
                            text = "Descripció:",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = recompensa!!.descripcio,
                            fontSize = 17.sp,
                            color = Color(0xFFAAAAAA)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Observacions:",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = recompensa!!.observacions,
                            fontSize = 17.sp,
                            color = Color(0xFFAAAAAA)
                        )
                    }

                    recompensaError?.let { errorMsg ->
                        Card(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 26.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            )
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                Arrangement.Center
                            ) {
                                Text(
                                    text = errorMsg,
                                    fontSize = 21.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ButtonsActionsRecompensa(
                            estat = recompensa!!.estat,
                            onReservar = {
                                recompensaViewModel.reservarRecompensa(
                                    recompensa!!.id,
                                    userState.value!!.email
                                )
                            },
                            onAnular = {
                                recompensaViewModel.anularReservaRecompensa(
                                    recompensa!!.id,
                                    userState.value!!.email
                                )
                            },
                            onRecollir = {
                                showDialog = true
                            }
                        )
                    }

                    // Diàleg per recollir
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = {
                                Text(
                                    text = recompensa!!.nom,
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            text = {
                                Text(
                                    text = "Punt bescanvi: ${recompensa!!.puntBescanvi}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            },
                            confirmButton = {
                                Button(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = {
                                        recompensaViewModel.recollirRecompensa(
                                            recompensa!!.id,
                                            userState.value!!.email
                                        )
                                        showDialog = false
                                        showEntregatImage = true
                                    }
                                ) {
                                    Text("Entregat")
                                }
                            }
                        )
                    }

                    // Pantalla ENTREGAT
                   /* if (showEntregatImage) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "ENTREGAT",
                                color = Color.Green,
                                fontSize = 48.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                            Icon(
                                painter = painterResource( R.drawable.success),
                                contentDescription = "ENTREGAT EXITOSAMENT",
                                tint = Color.Green
                            )
                        }
                    }*/

                }
            }
        }
    }
}

@Composable
fun ButtonsActionsRecompensa(
    estat: EstatRecompensa,
    onReservar: () -> Unit,
    onAnular: () -> Unit,
    onRecollir: () -> Unit
) {
    when (estat) {
        EstatRecompensa.DISPONIBLE -> Button(onClick = onReservar) { Text("Reservar") }
        EstatRecompensa.RESERVADA -> Button(onClick = onAnular) { Text("Anular Reserva") }
        EstatRecompensa.ASSIGNADA -> Button(onClick = onRecollir) { Text("Recollir") }
        else -> {}
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
