package com.example.km.core.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.util.Base64
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.km.core.models.Ruta
import com.example.km.core.models.Sistema
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.time.Duration
import java.time.LocalDateTime

fun base64ToByteArray(imageStr: String): ByteArray?{
    try {
        if (imageStr.isNullOrEmpty()) return null
        val imageBytes = Base64.decode(imageStr, Base64.DEFAULT)
        return imageBytes
    }catch (e: Exception){
        return null
    }
}


// IP POR DEFECTO DE LA MAQUINA VIRTUAL
const val IP_VM = "10.0.2.2"

// IP DE MI PORTATIL CONECTADO AL PUNTO DE ACCESO DE DATOS DE MI MOVIL
const val IP_PC = "192.168.67.227"
const val IP_PCWifiCasa = "192.168.1.107"

// URL DE CONEXION DE LA APP
const val URL_IP_APP = IP_VM

var sistema: Sistema? = null


@Composable
fun bitmapDescriptorFromVectorDp(
    @DrawableRes resId: Int,
    width: Dp = 32.dp,
    height: Dp = 32.dp
): BitmapDescriptor? {
    val context = LocalContext.current
    val density = LocalDensity.current

    val pxWidth = with(density) { width.toPx().toInt() }
    val pxHeight = with(density) { height.toPx().toInt() }

    val vectorDrawable = ContextCompat.getDrawable(context, resId) ?: return null
    vectorDrawable.setBounds(0, 0, pxWidth, pxHeight)

    val bitmap = Bitmap.createBitmap(pxWidth, pxHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/*
fun bitmapDescriptorFromVectorDp(
    context: Context,
    density: Density,
    @DrawableRes resId: Int,
    width: Dp = 32.dp,
    height: Dp = 32.dp
): BitmapDescriptor? {
    val pxWidth = with(density) { width.toPx().toInt() }
    val pxHeight = with(density) { height.toPx().toInt() }

    val vectorDrawable = ContextCompat.getDrawable(context, resId) ?: return null
    vectorDrawable.setBounds(0, 0, pxWidth, pxHeight)

    val bitmap = Bitmap.createBitmap(pxWidth, pxHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}*/

/*
@Composable
fun bitmapDescriptorFromVector(
    @DrawableRes resId: Int,
    width: Int = 64, // ancho en píxeles
    height: Int = 64 // alto en píxeles
): BitmapDescriptor? {
    val context = LocalContext.current
    val vectorDrawable = ContextCompat.getDrawable(context, resId) ?: return null

    vectorDrawable.setBounds(0, 0, width, height)

    val bitmap = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}*/