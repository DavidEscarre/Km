package com.example.km.core.utils

import android.util.Base64
import kotlinx.coroutines.CoroutineStart

fun base64ToByteArray(imageStr: String): ByteArray?{
    try {
        if (imageStr.isNullOrEmpty()) return null
        val imageBytes = Base64.decode(imageStr, Base64.DEFAULT)
        return imageBytes
    }catch (e: Exception){
        return null
    }
}