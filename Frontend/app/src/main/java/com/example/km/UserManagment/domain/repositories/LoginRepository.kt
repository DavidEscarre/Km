package com.example.km.UserManagment.domain.repositories

import com.example.km.core.models.User
import retrofit2.Response

interface LoginRepository {
    suspend fun loginUser(email: String, password: String): Response<User?>
}