package com.example.km.UserManagment.ui.viewmodels

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.core.auth.AuthRetrofitInstance
import com.example.km.core.utils.createPartFromString
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel()  {
    var email = mutableStateOf("")
    var word = mutableStateOf("")

    var emailError = mutableStateOf<String?>(null)
    var wordError = mutableStateOf<String?>(null)
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()


    fun validateFields(): Boolean {
        var isValid = true
        Log.d("VALIDATE_FIELDS", "EMAIL: ${email}")
        Log.d("VALIDATE_FIELDS", "word: ${word}")
/*
        if(!email.value.matches(emailRegex)){
            emailError.value = "Correu electronic inválid."
            isValid = false
        }else  emailError.value = null*/  //esta desavilitat per a proves

        if(word.value.length == 0){
            wordError.value = "La contrasenya no pot esta vuida"
            isValid = false
        }else  wordError.value = null

        return isValid
    }

    fun loginUser(context: android.content.Context, navController: androidx.navigation.NavController
    ,  onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validateFields()) return

        viewModelScope.launch {
            try {

                val response = AuthRetrofitInstance.authApi.loginUser(email.value, word.value)

                if (response.isSuccessful) {

                    Log.d("Login", "✅ login correcte")
                    onSuccess()
                } else {
                    Log.e("Login", "❌ Error en el Login ")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)

                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }
}