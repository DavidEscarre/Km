package com.example.km.UserManagment.data.datasource

import com.example.km.core.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApiRest {

    @POST("login")
    suspend fun loginUser(
        @Query("email") email: String,
        @Query("word") word: String
    ): Response<User?>

    @POST("forgotPassword")
    suspend fun forgotPassword(
        @Body email: String,
    ): Response<String?>

    @POST("verifyToken")
    suspend fun verifyToken(
        @Query("email") email: String,
        @Query("token") token: String
    ): Response<Unit?>

}


