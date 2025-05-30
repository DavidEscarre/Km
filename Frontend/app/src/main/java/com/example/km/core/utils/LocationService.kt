package com.example.km.core.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.km.R
import com.example.km.core.models.PuntGPS
import com.example.km.PuntGPSManagment.data.repositories.PuntGPSRepositoryImpl
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDateTime
/*
@RequiresApi(Build.VERSION_CODES.O)
class LocationService : Service() {
    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_UPDATE_PRECISION = "ACTION_UPDATE_PRECISION"
        const val EXTRA_INTERVAL = "EXTRA_INTERVAL_MS"
        const val CHANNEL_ID = "loc_channel"
        const val NOTIF_ID = 1001


    }
    var puntsGPSViewModel: PuntGPSViewModel? = null
    private val repo = PuntGPSRepositoryImpl()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var intervaloMs: Long = 5_000L
    private lateinit var locationRequest: LocationRequest
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(serviceJob + Dispatchers.IO)

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.locations.forEach { loc ->
                val punto = PuntGPS(loc.latitude, loc.longitude, LocalDateTime.now())
                puntsGPSViewModel?.let { it.setLocationListForeground(punto) }
            // serviceScope.launch { repo.createPuntGPS(punto) }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        buildLocationRequest()
    }

    @SuppressLint("MissingPermission", "ForegroundServiceType")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // Android Q+ needs type param here
                startForeground(NOTIF_ID, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION)
                startLocationUpdates()
            }
            ACTION_STOP -> {
                stopLocationUpdates()
                stopSelf()
            }
            ACTION_UPDATE_PRECISION -> intent.getLongExtra(EXTRA_INTERVAL, intervaloMs)
                .takeIf { it != intervaloMs }
                ?.let { newInterval ->
                    intervaloMs = newInterval
                    restartLocationUpdates()
                }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        stopLocationUpdates()
        serviceJob.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun buildLocationRequest() {
        locationRequest = LocationRequest.create().apply {
            interval = intervaloMs
            fastestInterval = intervaloMs / 2
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }
    }

    @Suppress("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun restartLocationUpdates() {
        stopLocationUpdates()
        buildLocationRequest()
        startLocationUpdates()
    }

    private fun buildNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Registrando ubicación")
        .setContentText("Actualización cada ${intervaloMs / 1000}s")
        .setSmallIcon(R.drawable.location_pin_lock_com)
        .build()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(CHANNEL_ID, "Servicio de Ubicación", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(chan)
        }
    }

    fun setPuntsViewModel(puntGPSViewModel: PuntGPSViewModel) {
        puntsGPSViewModel = puntGPSViewModel
    }

}
*/