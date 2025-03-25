package com.example.km.core.auth

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthRetrofitInstance {
    //Cuando se usa el emulador de movil desde AndroidStudio, la 10.0.2.2 de la red virtual apunta a localhost de la máquina anfitrion
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/rest/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApiRest by lazy {
        retrofit.create(AuthApiRest::class.java)
    }
}