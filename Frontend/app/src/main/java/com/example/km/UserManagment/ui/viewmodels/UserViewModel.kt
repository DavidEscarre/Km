package com.example.km.UserManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.UserManagment.data.repositories.UserRepositoryImpl
import com.example.km.core.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
class UserViewModel: ViewModel()  {


    private val userRepo = UserRepositoryImpl()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user


    fun findByEmail(email: String){

        viewModelScope.launch {
            val response =userRepo.getByEmail(email)
            if(response.isSuccessful){
                Log.d("UserViewModel", "Usuari trobat ${email}")
                _user.value = response.body()
            }else{
                Log.e("UserViewModel", "Usuari no trobat ${email}")
                _user.value = null
            }


        }
    }


    fun updatePasswordUser(email: String, newPassword: String){

        viewModelScope.launch {
            val response =userRepo.updatePassword(email, newPassword)
            if(response.isSuccessful){
                Log.d("UserViewModel", "Usuari actualitzat ${email}")

            }else{
                Log.e("UserViewModel", "Usuari no actualitzat ${email}")

            }


        }
    }

}