package com.example.km.UserManagment.domain.repositories

import com.example.km.core.models.User
import retrofit2.Response


interface UserRepository {

    suspend fun findAll(): Response<List<User>>


    suspend fun getByEmail(userEmail: String): Response<User>


    suspend fun createUser(user: User): Response<String>
    suspend fun deleteUserByEmail(userEmail: String): Response<Void>

    suspend fun updatePassword(userEmail: String, password: String): Response<User?>
}