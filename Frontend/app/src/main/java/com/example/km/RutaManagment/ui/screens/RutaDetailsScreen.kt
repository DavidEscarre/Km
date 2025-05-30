package com.example.km.RutaManagment.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.User
import com.example.km.core.utils.bitmapDescriptorFromVectorDp
import com.example.km.core.utils.enums.EstatRuta
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.Duration
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RutaDetailsScreen(
    rutaId: Long?,
    rutaViewModel: RutaViewModel,
    puntGPSViewModel: PuntGPSViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    userState: State<User?>
) {
    val user: User? = userState.value

    // Cámara y puntos
    val moveCameraTrigger = remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    var locationList by remember { mutableStateOf<List<LatLng>>(emptyList<LatLng>()) }
    var startPoint by remember { mutableStateOf<LatLng?>(null) }
    var endPoint by remember { mutableStateOf<LatLng?>(null) }
    var center by remember { mutableStateOf<LatLng?>(null) }

    // Overlay para lat/lng
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var showOverlay by remember { mutableStateOf(false) }

    val iconInicio = bitmapDescriptorFromVectorDp(
        resId = R.drawable.banderasstart,
        width = 64.dp,
        height = 64.dp
    )
    val iconFin = bitmapDescriptorFromVectorDp(
        resId = R.drawable.banderaend,
        width = 64.dp,
        height = 64.dp
    )

    // Observamos ruta y puntos
    val ruta by rutaViewModel.ruta.collectAsState()
    val punts by puntGPSViewModel.puntGPSRutaList.collectAsState()

    // Al cambiar rutaId, limpiamos datos previos y cargamos nuevos
    LaunchedEffect(rutaId) {
        rutaId?.let {
            locationList = emptyList()
            selectedLocation = null
            showOverlay = false
            startPoint = null
            endPoint = null
            center = null
            rutaViewModel.findById(it)
            puntGPSViewModel.fetchAllByRuteId(it)
        }
    }

    // Cuando llegan los puntos, actualizamos lista y cámara
    LaunchedEffect(punts) {
        if (punts.isNotEmpty()) {
            locationList = emptyList()
            punts.sortedBy { it.id }.forEach { punt ->
                locationList += LatLng(punt.latitud, punt.longitud)
            }
            startPoint = locationList.firstOrNull()
            endPoint = locationList.lastOrNull()
            val latAvg = locationList.map { it.latitude }.average()
            val lngAvg = locationList.map { it.longitude }.average()
            center = LatLng(latAvg, lngAvg)
            center?.let { cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(it, 17f)) }
        }
    }

    Scaffold(
        topBar = { TopBar("Detalls Ruta", user, userViewModel,navController) },
        bottomBar = { BottomNavigationBar(navController) })
    { paddingValues ->
        if (ruta == null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
            return@Scaffold
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mapa con tamaño fijo
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .border(BorderStroke(2.dp, Color.Black), RoundedCornerShape(6.dp))
            ) {

                    GoogleMap(
                        cameraPositionState = cameraPositionState,
                        modifier = Modifier.matchParentSize()
                    ) {
                        startPoint?.let { Marker(state = MarkerState(it), title = "Inici", icon = iconInicio) }
                        endPoint?.let { Marker(state = MarkerState(it), title = "Final", icon = iconFin) }
                        locationList.forEach { loc ->
                            Circle(
                                center = loc,
                                radius = 5.0,
                                strokeColor = Color.Red,
                                fillColor = Color.Red.copy(alpha = 0.7f),
                                clickable = true,
                                onClick = { selectedLocation = loc; showOverlay = true }
                            )
                        }
                        if (locationList.size > 1) Polyline(points = locationList, color = Color.Blue, width = 8f)
                    }

                if (showOverlay && selectedLocation != null) {
                    Box(
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(8.dp))
                            .border(BorderStroke(1.dp, Color.Gray), RoundedCornerShape(8.dp))
                            .clickable { showOverlay = false }
                            .padding(12.dp)
                    ) {
                        Column {
                            Text("Lat: %.5f".format(selectedLocation!!.latitude), fontWeight = FontWeight.Bold)
                            Text("Lng: %.5f".format(selectedLocation!!.longitude), fontWeight = FontWeight.Bold)
                            Text("Toca para cerrar", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
                IconButton(
                    onClick = { moveCameraTrigger.value = true },
                    modifier = Modifier.align(Alignment.BottomStart).padding(10.dp).size(50.dp)
                ) { Icon(painter = painterResource(R.drawable.center_camera), contentDescription = "Centrar cámara", tint = Color.Black.copy(alpha = 0.8f)) }
                LaunchedEffect(moveCameraTrigger.value) {
                    if (moveCameraTrigger.value && center != null) {
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(center!!, 17f), durationMs = 600)
                        moveCameraTrigger.value = false
                    }
                }
            }

            // Sección de datos con scroll
            Column(
                Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Card(
                    modifier = Modifier

                        .padding(20.dp)
                        .background(Color.Gray, RoundedCornerShape(25.dp))
                        .align(Alignment.CenterHorizontally)
                        .shadow(6.dp, RoundedCornerShape(25.dp)),

                    ) {

                    val end = ruta?.dataFinal ?: LocalDateTime.now()
                    val duration = Duration.between(ruta?.dataInici ?: LocalDateTime.now(), end)
                    val hours = duration.toHours()
                    val minutes = duration.minusHours(hours).toMinutes()
                    val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds
                    val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    val millisAturat = ruta?.tempsAturat ?: 0L
                    val totalSecondsAturat = millisAturat / 1000
                    val hoursAtur = totalSecondsAturat / 3600
                    val minutesAtur = ((totalSecondsAturat % 3600) / 60)
                    val secondsAtur = totalSecondsAturat % 60
                    val timeAturatText =
                        String.format("%02d:%02d:%02d", hoursAtur, minutesAtur, secondsAtur)

                    Row(Modifier.fillMaxWidth()) {
                        Column(
                            Modifier.fillMaxHeight().weight(1.65f)
                                .padding(top = 30.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                        ) {
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 30.sp
                                        )
                                    ) {
                                        append("Temps ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 25.sp
                                        )
                                    ) {
                                        append(timeText)
                                    }
                                }, textAlign = TextAlign.Center,
                                lineHeight = 32.sp
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 28.sp
                                        )
                                    ) {
                                        append("Temps aturat ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 25.sp
                                        )
                                    ) {
                                        append(timeAturatText)
                                    }
                                }, textAlign = TextAlign.Center,
                                lineHeight = 32.sp

                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 30.sp
                                        )
                                    ) {
                                        append("Distancia ")
                                    }

                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 25.sp
                                        )
                                    ) {
                                        append(ruta?.distancia.toString())
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Default,
                                            fontSize = 19.sp
                                        )
                                    ) {
                                        append("Km")
                                    }
                                },
                                textAlign = TextAlign.Center,
                                lineHeight = 32.sp

                            )

                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 24.sp
                                        )
                                    ) {
                                        append("Data: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 20.sp
                                        )
                                    ) {
                                        append(ruta?.dataInici?.toLocalDate().toString())
                                    }
                                }, textAlign = TextAlign.Center,
                                lineHeight = 32.sp
                            )
                            Spacer(Modifier.height(22.dp))


                        }
                        Column(Modifier.fillMaxHeight().weight(1f).padding(8.dp)) {

                            Text(
                                "Saldo",
                                Modifier.padding(top = 20.dp, start = 12.dp),
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )
                            val saldo = ruta?.saldo ?: 0.0
                            Card(
                                Modifier.padding(top = 8.dp).shadow(6.dp, RoundedCornerShape(8.dp)),
                                shape = RoundedCornerShape(8.dp),

                                colors = CardDefaults.cardColors(
                                    containerColor = if (saldo >= 0) Color(0xFF2ECC71) else Color(
                                        0xFFE74C3C
                                    ),
                                    contentColor = Color.White
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                ) {

                                    Text(
                                        text = "${if (saldo >= 0) "+" else "-"}${saldo}",
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
                            val isPendent = ruta?.estat == EstatRuta.PENDENT
                            val isValidada = ruta?.estat == EstatRuta.VALIDA
                            val container = if (isValidada && !isPendent) Color(0xFF2ECC71) else {
                                if (!isValidada && !isPendent) Color(0xFFE74C3C) else Color.Gray
                            }
                            val content = Color.White
                            Text(
                                "Estat",
                                Modifier.padding(top = 30.dp, start = 20.dp),
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )
                            Button(
                                onClick = { /* Acción si procede */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = container,
                                    contentColor = content,
                                    disabledContainerColor = container,
                                    disabledContentColor = content
                                ),
                                modifier = Modifier.padding(top = 10.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = if (isValidada && !isPendent) "Validada" else if (!isValidada && !isPendent) "No validada" else "Pendent",
                                    color = Color.White
                                )
                            }
                        }


                    }
                    Row(Modifier.fillMaxWidth()) {
                        Card(
                            Modifier.fillMaxHeight().fillMaxWidth()
                                .padding(start = 10.dp, bottom = 12.dp)
                                .shadow(4.dp, RoundedCornerShape(8.dp)),
                            colors = CardColors(
                                Color.DarkGray,
                                Color.DarkGray,
                                Color.DarkGray,
                                Color.DarkGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {

                            Text(
                                "Velocitat",
                                Modifier.align(Alignment.CenterHorizontally),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 30.sp
                            )

                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 28.sp
                                        )
                                    ) {
                                        append("Mitjana: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 23.sp
                                        )
                                    ) {
                                        append(ruta?.velocitatMitjana.toString())
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Default,
                                            fontSize = 17.sp
                                        )
                                    ) {
                                        append(" Km/h")
                                    }
                                },
                                Modifier.padding(10.dp),
                                color = Color.White,
                                textAlign = TextAlign.Left,
                                lineHeight = 32.sp
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Light,
                                            fontSize = 28.sp
                                        )
                                    ) {
                                        append("Maxima: ")
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontFamily = FontFamily.Monospace,
                                            fontSize = 23.sp
                                        )
                                    ) {
                                        append(ruta?.velocitatMax.toString())
                                    }
                                    withStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Default,
                                            fontSize = 17.sp
                                        )
                                    ) {
                                        append(" Km/h")
                                    }
                                },
                                Modifier.padding(10.dp),
                                color = Color.White,
                                textAlign = TextAlign.Left,
                                lineHeight = 32.sp

                            )
                        }
                    }

                }

            }
        }
    }
}
