package com.example.km.UserManagment.ui.viewmodels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.UserManagment.data.datasource.AuthRetrofitInstance
import com.example.km.UserManagment.data.repositories.LoginRepositoryImpl
import com.example.km.core.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
@RequiresApi(Build.VERSION_CODES.O)
class LoginViewModel: ViewModel()  {


    private val AuthRepo = LoginRepositoryImpl()


    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> get() = _userState


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

    @RequiresApi(Build.VERSION_CODES.O)
    fun loginUser(context: android.content.Context, navController: androidx.navigation.NavController
                  , onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!validateFields()) return

        viewModelScope.launch {
            try {


                val response = AuthRepo.loginUser(email.value, word.value)

                if (response.isSuccessful) {
                    if(response.body() != null){
                       // response.body().foto = response.body().foto
                        _userState.value = response.body()

                        Log.d("Login", "✅ login correcte")
                        onSuccess()

                        _loginError.value = null
                    }else{
                        Log.d("Login", "✅ Response Body Vuit pero Successful")
                    }

                } else {
                    _userState.value = null
                    Log.e("Login", "❌ Error en el Login, Codi: ${response.code()}")

                    if(response.code() == 404){
                        _loginError.value = "Usuari no trobat."
                        _emailError.value = "Aquest correu no pertany a cap usuari."
                    }else if(response.code() == 401){
                        _loginError.value = "Credencials incorrectes."
                    }else if(response.code() == 403){
                        _loginError.value = "Usuari no actiu."
                        _emailError.value = "Aquest correu pertany a un usuari inactiu."

                    }

                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "$errorBody"
                   // _loginError.value = errorBody
                    onError(errorMessage)

                }
            }catch(e: Exception){
                _userState.value = null
                e.printStackTrace()
            }
        }
    }
    fun logOut(context: android.content.Context, navController: androidx.navigation.NavController){

        viewModelScope.launch {

            _userState.value = null
            navController.navigate("login")
        }

    }
}