package com.example.km.RutaManagment.ui.screens

import android.os.Build
import android.text.Layout
import android.util.LayoutDirection
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.core.models.User
import com.example.km.core.utils.bitmapDescriptorFromVectorDp
import com.example.km.core.utils.enums.EstatRuta
import com.example.km.navigation.BottomNavigationBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.awaitAll
import java.time.Duration
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RutaDetailsScreen(
    rutaId: Long?,
    rutaViewModel: RutaViewModel,
    puntGPSViewModel: PuntGPSViewModel,
    navController: NavController,
    userState: State<User?>

) {
    val user: User? = userState.value



    val ruta by rutaViewModel.ruta.collectAsState()
    val moveCameraTrigger = remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val locationList = remember { mutableStateListOf<LatLng>() }
    var startPoint by remember { mutableStateOf<LatLng?>(null) }
    var endPoint   by remember { mutableStateOf<LatLng?>(null) }

    val context = LocalContext.current
    val density = LocalDensity.current

    val iconInicio =  bitmapDescriptorFromVectorDp(
            resId = R.drawable.banderasstart,
            width = 64.dp,
            height = 64.dp
    )

    val iconFin = bitmapDescriptorFromVectorDp(
            resId = R.drawable.banderaend,
            width = 64.dp,
            height = 64.dp
    )


  //  val iconInicio = bitmapDescriptorFromVector()
   // val iconFin = bitmapDescriptorFromVector(R.drawable.banderaend)
    var centro   by remember { mutableStateOf<LatLng?>(null) }
    if (rutaId != null){
        Log.d("RutaDetailsScreen", "fetching rutaId($rutaId)....")
        rutaViewModel.findById(rutaId)
    }else{
        Log.e("RutaDetailsScreen", "NO RUTA ID ....")
    }
    LaunchedEffect(Unit) {


        ruta?.puntsGPS?.let { punts ->

            val firstPunt = punts.minByOrNull { it.id } //id minima o primer punt
            val lastPunt  = punts.maxByOrNull { it.id } //id maxima o ultim punt
            Log.d("RutaDetailsScreen", "start: $startPoint, end: $endPoint, center: $centro")

            Log.d("RutaDetailsScreen", "location lsit size: ${locationList.size}")

            startPoint = firstPunt?.let { LatLng(it.latitud, it.longitud) }
            endPoint   = lastPunt?.let  { LatLng(it.latitud, it.longitud) }
            Log.d("RutaDetailsScreen", "start: $startPoint, end: $endPoint, center: $centro")
            locationList.clear()
            punts.forEach { punt ->
                locationList += LatLng(punt.latitud, punt.longitud)
            }

            //centrar camara en un punt mitja
            if (locationList.isNotEmpty()) {

                val LatMitjana = locationList.map { it.latitude }.average()
                val LngMitjana = locationList.map { it.longitude }.average()
                centro = LatLng(LatMitjana, LngMitjana)
                Log.d("RutaDetailsScreen", "start: $startPoint, end: $endPoint, center: $centro")

                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngZoom(centro!!,18f)
                )
            }
        }
    }


    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {

                Box(modifier = Modifier
                    .fillMaxWidth()  // Asegura que no se desborde
                    .height(400.dp)  // Altura razonable para el mapa
                    .clip( shape = RoundedCornerShape(bottomStartPercent =6, bottomEndPercent = 6))
                    .border(BorderStroke(2.dp,Color.Black), shape = RoundedCornerShape(bottomStartPercent =6, bottomEndPercent = 6))

                ){
                    // Muestra el mapa
                    GoogleMap(
                        cameraPositionState = cameraPositionState
                    ) {
                        startPoint?.let { location ->
                            Marker(anchor = Offset(0.3f, 0.80f),state = MarkerState(position = location), title = "Inici", icon= iconInicio)
                        }
                        endPoint?.let { location ->
                            Marker(anchor = Offset(0.31f, 0.76f),state = MarkerState(position = location), title = "final", icon= iconFin)
                        }

                        if (locationList.isNotEmpty()) {
                            Polyline(points = locationList, color = Color.DarkGray, width = 30f)
                        }
                    }

                    // Botón para centrar
                    IconButton(
                        onClick = { moveCameraTrigger.value = true },
                        modifier = Modifier
                            .align(Alignment.BottomStart)

                            .padding(bottom = 10.dp, start = 10.dp)
                            .size(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.center_camera),
                            contentDescription = "centrar camara",
                            tint = Color(0f,0f,0f,0.8f),
                            modifier = Modifier.size(76.dp)
                        )
                    }

                    // Solo mueve la cámara si se ha presionado el botón
                    LaunchedEffect(moveCameraTrigger.value) {
                        if (moveCameraTrigger.value && centro != null) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(centro!!, 17f),
                                durationMs = 600
                            )
                            moveCameraTrigger.value = false
                        }
                    }
                }

            Card(modifier = Modifier

                    .padding(20.dp)
                    .background(Color.Gray, RoundedCornerShape(25.dp))
                    .align(Alignment.CenterHorizontally)
                    .shadow(6.dp, RoundedCornerShape(25.dp)),
            ){
                // Calculate duration between start and end (or now)
                val end = ruta?.dataFinal ?: LocalDateTime.now()
                val duration = Duration.between(ruta?.dataInici, end)
                val hours = duration.toHours()
                val minutes = duration.minusHours(hours).toMinutes()
                val seconds = duration.minusHours(hours).minusMinutes(minutes).seconds
                val timeText = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                Row(Modifier.fillMaxSize()) {
                    Column(Modifier.fillMaxHeight().weight(1.6f).padding(top = 30.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)) {
                                    append("Temps ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 25.sp )) {
                                    append(timeText)
                                }
                            }, textAlign = TextAlign.Center,
                                lineHeight = 32.sp
                            )
                        Spacer(Modifier.height(22.dp))
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp)) {
                                    append("Distancia ")
                                }

                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace, fontSize = 25.sp )) {
                                    append(ruta?.distancia.toString())
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Default, fontSize = 19.sp )) {
                                    append("Km")
                                }
                            },
                                textAlign = TextAlign.Center,
                                lineHeight = 32.sp

                           )
                        Spacer(Modifier.height(22.dp))
                        Card(Modifier.fillMaxHeight().padding(start = 10.dp, bottom = 12.dp).shadow(4.dp,RoundedCornerShape(8.dp)),
                            colors = CardColors(Color.DarkGray,Color.DarkGray,Color.DarkGray,Color.DarkGray),
                            shape = RoundedCornerShape(8.dp)
                        ){

                            Text("Velocitat",Modifier.align(Alignment.CenterHorizontally), color = Color.White,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold, fontSize = 30.sp
                            )

                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 23.sp)) {
                                        append("Mitjana: ")
                                    }
                                    withStyle(style = SpanStyle(fontFamily = FontFamily.Monospace, fontSize = 19.sp )) {
                                        append(ruta?.velocitatMitjana.toString())
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Default, fontSize = 12.sp )) {
                                        append("Km/h")
                                    }
                                },
                                Modifier.padding(5.dp),
                                color = Color.White,
                                textAlign = TextAlign.Left,
                                lineHeight = 32.sp
                            )
                            Text(
                                buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Light, fontSize = 23.sp)) {
                                        append("Maxima: ")
                                    }
                                    withStyle(style = SpanStyle(fontFamily = FontFamily.Monospace, fontSize = 19.sp )) {
                                        append(ruta?.velocitatMax.toString())
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Default, fontSize = 12.sp )) {
                                        append("Km/h")
                                    }
                                },
                                Modifier.padding(5.dp),
                                color = Color.White,
                                textAlign = TextAlign.Left,
                                lineHeight = 32.sp

                            )
                        }

                    }
                    Column(Modifier.fillMaxHeight().weight(1f).padding(10.dp)) {

                        Text("Saldo",Modifier.padding(top = 20.dp, start = 12.dp), color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold, fontSize = 30.sp
                        )
                        Card(
                            Modifier.padding(top = 8.dp).shadow(6.dp,RoundedCornerShape(8.dp)),
                            shape = RoundedCornerShape(8.dp),

                            colors = CardDefaults.cardColors(
                                containerColor = if (ruta?.saldo!! >= 0) Color(0xFF2ECC71) else Color(0xFFE74C3C),
                                contentColor = Color.White
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {

                                Text(
                                    text = "${if (ruta?.saldo!! >= 0) "+" else "-"}${ruta?.saldo}",
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
                        val isValidada = ruta?.estat == EstatRuta.VALIDA
                        val container = if (isValidada) Color(0xFF2ECC71) else Color(0xFFE74C3C)
                        val content = Color.White
                        Text("Estat",Modifier.padding(top = 30.dp, start = 20.dp), color = Color.DarkGray,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold, fontSize = 30.sp
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
                                text = if (isValidada) "Validada" else "No validada",
                                color = Color.White
                            )
                        }
                    }


                }

            }

        }

    }
}
