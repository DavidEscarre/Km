package com.example.km.UserManagment.data.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.km.UserManagment.data.datasource.UserApiRest
import com.example.km.UserManagment.data.datasource.UserRetrofitInstance
import com.example.km.UserManagment.domain.repositories.UserRepository
import com.example.km.core.models.User
import retrofit2.Response

@RequiresApi(Build.VERSION_CODES.O)
class UserRepositoryImpl: UserRepository {


    val userApiRest: UserApiRest =  UserRetrofitInstance.userApi

    override suspend fun findAll(): Response<List<User>> {
        val response = userApiRest.findAll()
        return response
    }

    override suspend fun getByEmail(userEmail: String): Response<User> {
        val response = userApiRest.getByEmail(userEmail)
        return response
    }
    override  suspend fun createUser(user: User): Response<String>{
        val response = userApiRest.createUser(user)
        return response
    }
    override suspend fun deleteUserByEmail(userEmail: String): Response<Void> {
        val response = userApiRest.deleteUserByEmail(userEmail)
        return response
    }






}