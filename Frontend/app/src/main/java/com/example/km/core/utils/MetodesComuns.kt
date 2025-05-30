package com.example.km.core.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.km.core.models.Sistema
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
const val IP_DOCKER = "54.123.45.67"
// URL DE CONEXION DE LA APP
const val URL_IP_APP = IP_VM;

var sistema: Sistema? = null


fun base64ToBitmap(base64Str: String?): Bitmap? {
    return try {
        if (base64Str.isNullOrEmpty()) return null
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}


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




// Funció per formatejar si existeix; admet ISO date o ISO date-time
@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String?): String? = dateString?.let { str ->
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    try {
        // Si ve amb hora, parseja com a LocalDateTime
        val date = if (str.contains("T")) {
            LocalDateTime.parse(str).toLocalDate()
        } else {
            LocalDate.parse(str)
        }
        date.format(formatter)
    } catch (e: Exception) {
        // En cas d'error, retorna el text original
        str
    }
}

/*
fun byteArrayToImageBitmap(bytes: ByteArray): ImageBitmap {
    val bitmap = byteArrayToBitmap(bytes)
    return bitmap.asImageBitmap()
}


fun base64ToImageBitmap(imageStr: String): ImageBitmap?{
    try {
        if (imageStr.isNullOrEmpty()) return null
        val imageBytes = Base64.decode(imageStr, Base64.DEFAULT)

        val bitmap = byteArrayToBitmap(imageBytes)
        return bitmap.asImageBitmap()

    }catch (e: Exception){
        return null
    }
}*/