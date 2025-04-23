package com.example.km.SistemaManagment.data.repositories

import android.util.Log
import com.example.km.SistemaManagment.data.datasource.SistemaApiRest
import com.example.km.SistemaManagment.data.datasource.SistemaRetrofitInstance
import com.example.km.SistemaManagment.domain.repositories.SistemaRepository
import com.example.km.core.models.Sistema
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Response


class SistemaRepositoryImpl: SistemaRepository {


    private val _sistema = MutableStateFlow<Sistema?>(null)
    val sistema: StateFlow<Sistema?> get() = _sistema


    val sistemaApiRest: SistemaApiRest =  SistemaRetrofitInstance.retrofitInstance.create(SistemaApiRest::class.java)




    override suspend fun findAll(): Response<List<Sistema>> {
        val response = sistemaApiRest.findAll()
        if(response.isSuccessful) {
            if (response.body() != null) {
                val sistemaObtingut = response.body()!!.first()
                Log.d(
                    "SistemaRepo11111",
                    "Sistema trobat i carregat correctament precicioPunts ${sistemaObtingut.precisioPunts}"
                )
                Log.d(
                    "SistemaRepo11111",
                    "Sistema trobat i carregat correctament puntsKm ${sistemaObtingut.puntsKm}"
                )

                _sistema.value = sistemaObtingut

            }
        }else{
            Log.d(
                "SistemaRepo11111",
                "FAAAAIIIIL ${response.errorBody()}"
            )
        }
        return response
    }
    override suspend fun findFirst(): Response<List<Sistema>> {
        val response = sistemaApiRest.findAll()
        if(response.isSuccessful){
            if(response.body() != null){
                val sistemaObtingut = response.body()!!.first()

                _sistema.value = sistemaObtingut

            }
        }
        return response
    }
    override suspend fun getSistemaById(sistemaId: Long): Response<Sistema?> {
       val response = sistemaApiRest.getSistemaById(sistemaId)

        if(response.isSuccessful){
            if(response.body() != null){
                val sistemaObtingut = response.body()!!

                Log.d("SistemaRepo", "Sistema trobat i carregat correctament puntsKm ${sistemaObtingut.puntsKm}")

                _sistema.value = sistemaObtingut
                Log.d("SistemaRepo", "Sistema trobat i carregat correctament precicioPunts ${sistema.value?.precisioPunts}")
            }else{
                Log.e("SistemaRepo","Error al obtenir el sistema")
            }
        }

        return response
    }
}