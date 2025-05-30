package com.example.km.SistemaManagment.data.datasource

import com.example.km.RutaManagment.data.datasource.RutaRetrofitInstance
import com.example.km.core.utils.URL_IP_APP
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SistemaRetrofitInstance {
    //Cuando se usa el emulador de movil desde AndroidStudio, la 10.0.2.2 de la red virtual apunta a localhost de la máquina anfitrion
    private const val BASE_URL = "http://$URL_IP_APP:8080/rest/sistema/"  // Reemplaza con la URL de tu servidor Spring Boot
  //  private const val BASE_URL = "https://$URL_IP_APP:8443/rest/sistema/"  // Reemplaza con la URL de tu servidor Spring Boot


    val retrofitInstance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Convierte JSON a objetos Kotlin
            .build()
    }

    fun <T> create(service: Class<T>): T {
        return retrofitInstance.create(service)
    }
}