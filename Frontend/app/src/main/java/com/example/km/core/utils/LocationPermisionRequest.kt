package com.example.km.core.utils
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.core.content.ContextCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.SistemaManagment.ui.viewmodels.SistemaViewModel
import com.example.km.core.models.User
import com.example.km.main.screens.AturarRuta
import com.example.km.main.screens.IniciarRuta
/*
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun PermisosYBotonIniciarServicio(
    sistemaViewModel: SistemaViewModel,
    rutaViewModel: RutaViewModel,
    puntGPSViewModel: PuntGPSViewModel,
    userState: State<User?>
) {
    val context = LocalContext.current

    // 1) Estados de permiso
    var fineGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var bgGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContextCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    // 2) Launchers
    val fineLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> fineGranted = granted }

    val bgLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> bgGranted = granted }

    // 3) Pedir permisos al mostrar
    LaunchedEffect(Unit) {
        if (!fineGranted) fineLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        else if (!bgGranted) bgLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    // 4) Lógica de botón
    var rutaActiva by rememberSaveable { mutableStateOf(false) }
    var activadorRuta by remember { mutableStateOf(false) }

    Button(onClick = {
        if (!fineGranted || !bgGranted) {
            Toast.makeText(context, "Debes conceder permisos de ubicación", Toast.LENGTH_SHORT).show()
            return@Button
        }
        rutaActiva = !rutaActiva
        activadorRuta = true
        val svcIntent = Intent(context, LocationService::class.java).apply {
            action = if (rutaActiva) LocationService.ACTION_START else LocationService.ACTION_STOP
        }
        if (rutaActiva)
            ContextCompat.startForegroundService(context, svcIntent)
        else
            context.stopService(svcIntent)
    }) {
        Text(if (rutaActiva) "Parar Ruta" else "Comenzar Ruta")
    }

    // 5) Disparar tus composables de inicio/parada de ruta
    if (activadorRuta) {
        if (rutaActiva) {
            IniciarRuta(rutaViewModel, puntGPSViewModel, context, userState)
        } else {
            AturarRuta(false, rutaViewModel, puntGPSViewModel, context, userState)
        }
        activadorRuta = false
    }
}
*/