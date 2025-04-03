package com.example.km.PuntGPSManagment.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.core.utils.LocalDateTimeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
object PuntGPSRetrofitInstance {
    @RequiresApi(Build.VERSION_CODES.O)
    var gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
    //Cuando se usa el emulador de movil desde AndroidStudio, la 10.0.2.2 de la red virtual apunta a localhost de la máquina anfitrion
    private const val BASE_URL = "http://10.0.2.2:8080/rest/puntgps/"  // Reemplaza con la URL de tu servidor Spring Boot


    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))  // Convierte JSON a objetos Kotlin
            .build()
    }
}