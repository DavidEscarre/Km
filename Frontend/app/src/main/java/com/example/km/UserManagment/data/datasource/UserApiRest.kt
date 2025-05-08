package com.example.km.UserManagment.data.datasource

import com.example.km.core.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiRest {

    @GET("all")
    suspend fun findAll(): Response<List<User>>

    @GET("byEmail/{userEmail}")
    suspend fun getByEmail(@Path("userEmail") userEmail: String): Response<User>

    @POST("create")
    suspend fun createUser(@Body user: User): Response<String>

    @DELETE("delete/{userEmail}")
    suspend fun deleteUserByEmail(@Path("userEmail") userEmail: String): Response<Void>

    @PUT("updatePassword/{userEmail}")
    suspend fun updatePassword(@Path("userEmail") userEmail: String, @Body password: String): Response<User?>



    }


