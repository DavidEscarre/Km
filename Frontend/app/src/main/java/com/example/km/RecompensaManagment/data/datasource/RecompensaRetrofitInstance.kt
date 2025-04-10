package com.example.km.RecompensaManagment.data.datasource

import com.example.km.core.utils.URL_IP_APP
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RecompensaRetrofitInstance {
    //Cuando se usa el emulador de movil desde AndroidStudio, la 10.0.2.2 de la red virtual apunta a localhost de la máquina anfitrion
    private const val BASE_URL ="http://$URL_IP_APP:8080/rest/puntgps/"  // Reemplaza con la URL de tu servidor Spring Boot


    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Convierte JSON a objetos Kotlin
            .build()
    }
}