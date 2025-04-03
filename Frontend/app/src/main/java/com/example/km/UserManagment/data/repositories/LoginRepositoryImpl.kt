package com.example.km.UserManagment.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.UserManagment.data.datasource.AuthApiRest
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.UserManagment.domain.repositories.LoginRepository
import com.example.km.core.models.User
import retrofit2.Response
@RequiresApi(Build.VERSION_CODES.O)
class LoginRepositoryImpl: LoginRepository {


    val authApiRest: AuthApiRest=  AuthRetrofitInstance.authApi


    override suspend fun loginUser(email: String, word: String): Response<User?> {
        val response = authApiRest.loginUser(email, word)
        return response
    }

}