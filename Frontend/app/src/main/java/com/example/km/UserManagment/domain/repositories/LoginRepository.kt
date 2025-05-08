package com.example.km.UserManagment.domain.repositories

import com.example.km.core.models.User
import retrofit2.Response

interface LoginRepository {
    suspend fun loginUser(email: String, word: String): Response<User?>

    suspend fun forgotPassword(email: String): Response<String?>

    suspend fun verifyToken(email: String, token: String): Response<Unit?>
}