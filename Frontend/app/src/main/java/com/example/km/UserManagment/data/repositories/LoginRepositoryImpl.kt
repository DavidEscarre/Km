package com.example.km.UserManagment.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.UserManagment.data.datasource.AuthApiRest
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.UserManagment.domain.repositories.LoginRepository
import com.example.km.core.models.User
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

@RequiresApi(Build.VERSION_CODES.O)
class LoginRepositoryImpl: LoginRepository {


    val authApiRest: AuthApiRest=  AuthRetrofitInstance.authApi


    override suspend fun loginUser(email: String, word: String): Response<User?> {
        val response = authApiRest.loginUser(email, word)
        return response
    }

    override suspend fun forgotPassword(email: String): Response<String?> {
        val response = authApiRest.forgotPassword(email)
        return response
    }

    override suspend fun verifyToken(email: String, token: String): Response<Unit?> {
        val response = authApiRest.verifyToken(email, token)
        return response
    }

}