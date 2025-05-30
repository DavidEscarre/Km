package com.example.km.main.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.SistemaManagment.ui.viewmodels.SistemaViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.Ruta
import com.example.km.core.models.Sistema
import com.example.km.core.models.User
//import com.example.km.core.utils.LocationService
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun MainScreen(
    sistemaViewModel: SistemaViewModel,
    rutaViewModel: RutaViewModel,
    puntGPSViewModel: PuntGPSViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    userState: State<User?>
) {
    val context = LocalContext.current
    val user = userState.value

    // Permisos de ubicación
    var fineGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    var bgGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
            else true
        )
    }
    val fineLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> fineGranted = granted }
    val bgLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> bgGranted = granted }

    LaunchedEffect(Unit) {
        if (!fineGranted) fineLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
    LaunchedEffect(fineGranted) {
        if (fineGranted
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
            && !bgGranted
        ) {
            bgLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
    }

    // Estado de ruta
    var rutaActiva by rememberSaveable { mutableStateOf(false) }
    var activadorRuta by remember { mutableStateOf(false) }

    // Estados del mapa y rutas
    val moveCameraTrigger = remember { mutableStateOf(false) }
    val cameraPositionState = rememberCameraPositionState()
    val userLocation by puntGPSViewModel.currentLocation.collectAsState()
    val aturat by puntGPSViewModel.aturat.collectAsState()
    val tempsAtur by puntGPSViewModel.tempsAtur.collectAsState()
    val tepsMaxAtur by sistemaViewModel.tempsMaxAtur.collectAsState()
    val locationList by puntGPSViewModel.locationList.collectAsState()

    // Inicialización sistema
    val sistema = getSistemaFromBackend(sistemaViewModel)
 //   puntGPSViewModel.updateServicePrecision()
    puntGPSViewModel.setPrecisio(sistema.precisioPunts)
    sistemaViewModel.findFirst()

    Scaffold(
        topBar = { TopBar("Home", user, userViewModel, navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            // Mapa
            Box(
                Modifier
                    .border(2.dp, Color.Black, RoundedCornerShape(4.dp))
                    .fillMaxWidth()
                    .height(600.dp)
            ) {
                if (fineGranted) {
                    LaunchedEffect(Unit) {
                        LocationServices
                            .getFusedLocationProviderClient(context)
                            .lastLocation
                            .addOnSuccessListener { loc ->
                                loc?.let {
                                    val latlng = LatLng(it.latitude, it.longitude)
                                    puntGPSViewModel.setCurrentLocation(latlng)
                                    cameraPositionState.position =
                                        CameraPosition.fromLatLngZoom(latlng, 15f)
                                }
                            }
                        puntGPSViewModel.startLocationUpdatesSimple()
                    }

                    GoogleMap(cameraPositionState = cameraPositionState) {
                        userLocation?.let {
                            Marker(MarkerState(it), title = "Mi Ubicación")
                        }
                        if (locationList.isNotEmpty()) {
                            Polyline(points = locationList, width = 8f, color = Color.Blue)
                        }
                    }

                    IconButton(
                        onClick = { moveCameraTrigger.value = true },
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(10.dp)
                            .size(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.center_camera),
                            contentDescription = "Centrar cámara"
                        )
                    }

                    LaunchedEffect(moveCameraTrigger.value) {
                        if (moveCameraTrigger.value && userLocation != null) {
                            cameraPositionState.animate(
                                CameraUpdateFactory.newLatLngZoom(userLocation!!, 17f),
                                600
                            )
                            moveCameraTrigger.value = false
                        }
                    }
                }
            }

            Spacer(Modifier.height(21.dp))

            // Botón iniciar/parar ruta
            Button(
                onClick = {
                    if (!fineGranted || !bgGranted) {
                        Toast
                            .makeText(context, "Concede permisos de ubicación", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }
                    rutaActiva = !rutaActiva
                    activadorRuta = true
                    /*if (rutaActiva) puntGPSViewModel.startLocationService()
                    else puntGPSViewModel.stopLocationService()*/
                },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterHorizontally)
                    .shadow(5.dp, RoundedCornerShape(50))
            ) {
                Text(
                    text = if (rutaActiva) "Aturar Ruta" else "Començar Ruta",
                    fontSize = 18.sp
                )
            }

            // Temporizador de parada
            if (aturat && rutaActiva) {
                val remaining = (tepsMaxAtur - tempsAtur).coerceAtLeast(0L)
                val minutes = (remaining / 1000) / 60
                val seconds = (remaining / 1000) % 60
                Text(
                    buildAnnotatedString {
                        append("Parada detectada, falta ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("${minutes}m ${seconds}s")
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            // Disparar lógica fuera onClick
            if (activadorRuta) {
                if (rutaActiva) IniciarRuta(rutaViewModel, puntGPSViewModel, context, userState)
                else AturarRuta(false, rutaViewModel, puntGPSViewModel, context, userState)
                activadorRuta = false
            }
        }
    }
}


@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            Toast.makeText(context, "Permiso de ubicación denegado", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}


@SuppressLint("MissingPermission")
fun getCurrentLocation(context: Context, onLocationReceived: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        location?.let {
            onLocationReceived(LatLng(it.latitude, it.longitude))

        }




    }
}



@SuppressLint("CoroutineCreationDuringComposition", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun IniciarRuta(
    rutaViewModel: RutaViewModel,
    puntsGPSViewModel: PuntGPSViewModel,
    context: Context,
    userState: State<User?>
) {
    // Dejar de las actualizaciones simples antes de arrancar el servicio
    puntsGPSViewModel.stopLocationUpdatesSimple()

    val coroutineScope = rememberCoroutineScope()
    val dataInici = LocalDateTime.now()
    val rutaIniciada by rutaViewModel.rutaIniciada.collectAsState()
    val ciclista = userState.value ?: return

    if (rutaIniciada == null) {
        // Creamos la ruta y la enviamos al backend
        val ruta = Ruta(ciclista, dataInici)
        coroutineScope.launch {
            rutaViewModel.iniciarRuta(
                ruta = ruta,
                context = context,
                onSuccess = {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Ruta iniciada", Toast.LENGTH_SHORT).show()
                    }
                    // Una vez iniciada la ruta en backend, arrancamos el servicio de localización
                    puntsGPSViewModel.startLocationUpdates()
                },
                onError = { errorMessage ->
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AturarRuta(
    aturPerTemps: Boolean,
    rutaViewModel: RutaViewModel,
    puntsGPSViewModel: PuntGPSViewModel,
    context: Context,
    userState: State<User?>
) {
    // Paramos el servicio de localización en primer plano
   // puntsGPSViewModel.stopLocationService()
    puntsGPSViewModel.stopLocationUpdates()
    val coroutineScope = rememberCoroutineScope()
    val puntsGPSlist by puntsGPSViewModel.puntGPSRutaListActual.collectAsState()
    val rutaActual = rutaViewModel.rutaAct.collectAsState().value
    val ciclista = userState.value
    val tempsAturAcumulatRuta by puntsGPSViewModel.tempsAturAcumulatRuta.collectAsState()
    val dataFinal = LocalDateTime.now()

    if (ciclista == null) {
        Toast.makeText(context, "Error: Usuario no encontrado", Toast.LENGTH_SHORT).show()
        return
    }

    if (rutaActual != null) {
        // Creamos el objeto Ruta con la hora de fin y el tiempo de parada acumulado
        val ruta = Ruta(
            rutaActual.id,
            ciclista,
            rutaActual.dataInici,
            dataFinal,
            tempsAturAcumulatRuta
        )

        coroutineScope.launch {
            rutaViewModel.aturarRuta(
                ruta = ruta,
                puntsGPSList = puntsGPSlist,
                context = context,
                onSuccess = {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            if (aturPerTemps) "Temps d'atur màxim superat" else "Ruta finalizada",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    puntsGPSViewModel.vuidarllistaPuntsGPS()
                },
                onError = { errorMessage ->
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }

    // Reset de temporizador y lista de puntos en el ViewModel
    puntsGPSViewModel.ResetTempsAtur()
    puntsGPSViewModel.vuidarllistaPuntsGPS()
    // Volver a iniciar actualizaciones simples para centrar mapa tras detener ruta
    puntsGPSViewModel.startLocationUpdatesSimple()
}


@RequiresApi(Build.VERSION_CODES.O)
fun getSistemaFromBackend(sistemaViewModel: SistemaViewModel): Sistema {
    sistemaViewModel.findById(1)
    val sistema = sistemaViewModel.sistema.value
    return if (sistema == null) {
        Log.e("Error_Sistema_MainAct", "Error al cargar el sistema")
        Sistema()
    } else {
        Log.d("Sistema_MainAct", "Sistema cargado correctamente")
        sistema
    }
}