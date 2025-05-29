package com.example.km.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.km.R
import com.example.km.PuntGPSManagment.data.repositories.PuntGPSRepositoryImpl
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.core.models.PuntGPS
import com.google.android.gms.location.*
import java.time.LocalDateTime
/*
@RequiresApi(Build.VERSION_CODES.O)
class LocationService(puntGPSViewModel: PuntGPSViewModel) : Service() {

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_UPDATE_PRECISION = "ACTION_UPDATE_PRECISION"
        const val EXTRA_INTERVAL = "EXTRA_INTERVAL_MS"
    }


    private val repo = PuntGPSRepositoryImpl()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Valor por defecto: 5000 ms
    private var intervaloMs: Long = 5_000L

    // Se inicializa en onCreate y se reconstruye al cambiar intervaloMs
    private lateinit var locationRequest: LocationRequest

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach { loc ->
                // Persistir en tu repositorio
                val punto = PuntGPS(loc.latitude, loc.longitude, LocalDateTime.now())

                puntGPSViewModel.


            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        buildLocationRequest() // Crea el primer locationRequest
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Arranca en foreground y solicita ubicaciones
                startForeground(1, buildNotification())
                startLocationUpdates()
            }
            ACTION_STOP -> stopSelf()
            ACTION_UPDATE_PRECISION -> intent.getLongExtra(EXTRA_INTERVAL, intervaloMs)
                .takeIf { it != intervaloMs }
                ?.let { newInterval ->
                    updatePrecision(newInterval)
                }
        }
        return START_STICKY
    }

    /**
     * Cambia la precisión/intervalo de actualización y reinicia el tracking
     */
    fun updatePrecision(nuevoIntervaloMs: Long) {
        intervaloMs = nuevoIntervaloMs
        restartLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    /** Crea o actualiza el LocationRequest según `intervaloMs` */
    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = intervaloMs
            fastestInterval = intervaloMs / 2
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }

    private fun restartLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
        buildLocationRequest()
        startLocationUpdates()
    }

    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun buildNotification() = NotificationCompat.Builder(this, "loc_channel")
        .setContentTitle("Registrando ubicación")
        .setContentText("Actualización cada ${intervaloMs / 1000}s")
        .setSmallIcon(R.drawable.ic_location)
        .build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(
                "loc_channel",
                "Servicio de Ubicación",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(chan)
        }
    }
}*/