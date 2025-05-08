package com.example.km.UserManagment.data.datasource

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.core.utils.LocalDateTimeAdapter
import com.example.km.core.utils.URL_IP_APP
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
object AuthRetrofitInstance {

    @RequiresApi(Build.VERSION_CODES.O)
    var gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()
    //Cuando se usa el emulador de movil desde AndroidStudio, la 10.0.2.2 de la red virtual apunta a localhost de la máquina anfitrion
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://$URL_IP_APP:8080/api/auth/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }




    val authApi: AuthApiRest by lazy {
        retrofit.create(AuthApiRest::class.java)
    }
}