package com.example.km.RutaManagment.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.core.utils.LocalDateTimeAdapter
import com.example.km.core.utils.URL_IP_APP
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
object RutaRetrofitInstance {

    /*val client = OkHttpClient.Builder()
        .followRedirects(false)  // No seguir redirecciones HTTP
        .followSslRedirects(false)  // No seguir redirecciones HTTPS
        .build()*/

    @RequiresApi(Build.VERSION_CODES.O)
    var gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    //Cuando se usa el emulador de movil desde AndroidStudio, la 10.0.2.2 de la red virtual apunta a localhost de la máquina anfitrion

   private const val BASE_URL = "http://$URL_IP_APP:8443/rest/rutes/"  // Reemplaza con la URL de tu servidor Spring Boot
   // private const val BASE_URL = "http://$URL_IP_APP:8080/rest/rutes/"  // Reemplaza con la URL de tu servidor Spring Boot
/*
    val credential = Credentials.basic("usuario", "contraseña")

    val request = Request.Builder()
        .url("http://10.0.2.2:8080/rest/rutes/create")
        .header("Authorization", credential)
        .post(RequestBody.create("application/json".toMediaType(), "{}"))
        .build()*/

    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))  // Convierte JSON a objetos Kotlin
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofitInstance.create(service)
    }
}