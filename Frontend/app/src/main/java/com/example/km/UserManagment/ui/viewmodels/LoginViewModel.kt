package com.example.km.UserManagment.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel()  {

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private val _word = MutableStateFlow<String>("")
    val word: StateFlow<String> get() = _word

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?>  get() = _emailError
    private val _wordError = MutableStateFlow<String?>(null)
    val wordError: StateFlow<String?> get() = _wordError

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()

    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        validateFields()
    }

    fun onPasswordChange(newPassword: String) {
        _word.value = newPassword
        validateFields()
    }

    fun validateFields(): Boolean {
        var isValid = true
        Log.d("VALIDATE_FIELDS", "EMAIL: ${_email.value}")
        Log.d("VALIDATE_FIELDS", "word: ${_word.value}")

        //Esta desavilitat per a proves

        if(!email.value.matches(emailRegex)){
            _emailError.value = "Correu electronic inválid."
            isValid = false
        }else  _emailError.value = null

        if(_word.value.length == 0){
            _wordError.value = "La contrasenya no pot esta vuida."
            isValid = false
        }else  _wordError.value = null

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
                    _loginError.value = null
                } else {

                    Log.e("Login", "❌ Error en el Login ")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "$errorBody"
                   // _loginError.value = errorBody
                    onError(errorMessage)
                    _loginError.value = errorMessage
                }
            }catch(e: Exception){
                e.printStackTrace()
            }
        }
    }
}