package com.example.km.core.auth

import com.example.km.core.models.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface AuthApiRest {

    @POST("login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("password") password: String
    ): Response<User>

    @Multipart
    @POST("register")
    suspend fun registerUser(
        @Part("nom") nom: RequestBody,
        @Part("email") email: RequestBody,
        @Part("telefon") telefon: RequestBody,
        @Part("word") word: RequestBody,
        @Part("adreca") adreca: RequestBody,
        @Part("observacions") observacions: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<User>

}


