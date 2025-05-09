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

    private val _emailRecContr = MutableStateFlow<String>("")
    val emailRecContr: StateFlow<String> get() = _emailRecContr

    private val _wordRecContr = MutableStateFlow<String>("")
    val wordRecContr: StateFlow<String> get() = _wordRecContr

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?>  get() = _emailError
    private val _wordError = MutableStateFlow<String?>(null)
    val wordError: StateFlow<String?> get() = _wordError

    private val _confirmWordError = MutableStateFlow<String?>(null)
    val confirmWordError: StateFlow<String?> get() = _confirmWordError


    private val _emailRecContrError = MutableStateFlow<String?>(null)
    val emailRecContrError: StateFlow<String?>  get() = _emailRecContrError
    private val _wordRecContrError = MutableStateFlow<String?>(null)
    val wordRecContrError: StateFlow<String?> get() = _wordRecContrError
    private val _tokenError = MutableStateFlow<String?>(null)
    val tokenError: StateFlow<String?> get() = _tokenError

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError


    private val _RecContrError = MutableStateFlow<String?>(null)
    val RecContrError: StateFlow<String?> get() = _RecContrError

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
    val tokenRegex ="^[A-Z0-9]{6}$".toRegex()

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
    fun verifyToken(email: String, token: String, onSuccess: () -> Unit, onError: (String) -> Unit ){

        viewModelScope.launch {
            try{
                val response = AuthRepo.verifyToken(email, token)
                if(response.isSuccessful){
                    onSuccess()

                    _RecContrError.value = null
                }else{

                    Log.e("LoginViewM:VerifyToken", "❌ Error al verificar el token de recuperacion.")
                    val errorBody = response.errorBody()?.string()
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"

                    _RecContrError.value = errorBody
                    onError(errorMessage)
                }
            }catch(e: Exception){
               // _RecContrError.value = "Error intern del servidor"
                e.printStackTrace()
            }


        }

    }
    fun forgotPassword(email: String, onSuccess: () -> Unit, onError: (String) -> Unit ){

        viewModelScope.launch {
            try {

                val response = AuthRepo.forgotPassword(email)
                if(response.isSuccessful){
                    if(response.body().equals("token Enviado")){
                        _RecContrError.value = null
                        onSuccess()
                    }else{
                        _RecContrError.value = response.body().toString()
                        onError(response.body().toString())
                    }
                }else{
                    Log.e("LoginViewM:ForgotPasswd", "❌ Error enviar el correo electronico de recuperacion")
                    val errorBody = response.errorBody()?.string()
                    _RecContrError.value = errorBody
                    val errorMessage =
                        "❌ ${response.code()} - $errorBody"
                    onError(errorMessage)
                }

            }catch(e: Exception){
              //  _RecContrError.value = "Error intern del servidor"
                e.printStackTrace()
            }
        }
    }

    fun onEmailRecContrChange(newEmail: String) {
        _emailRecContr.value = newEmail
        validateEmail(newEmail)
    }

    fun onTokenChange(token: String) {
        validateToken(token)
    }


    fun onPasswordRecContrChange(newPassword: String, confirmWord: String) {
        _wordRecContr.value = newPassword
        validatePasswords(newPassword, confirmWord)
    }

    fun validateToken(token: String): Boolean {
        var isValid = true
        Log.d("UserVM-VALIDATE_Token ","Token: ${token}")

        if(!token.matches(tokenRegex)){
            _tokenError.value = "EL token ha de tenir 6 caracters (A-Z i 0-9)."
            isValid = false
        }else{
            _tokenError.value = null
        }
        return isValid
    }


    fun validateEmail(email: String): Boolean {
        var isValid = true
        Log.d("UserVM-VALIDATE_Email ","EMAIL: ${email}")

        if(!email.matches(emailRegex)){
            _emailRecContrError.value = "Correu electronic inválid."
            isValid = false
        }else{

            _emailRecContrError.value = null
        }
        return isValid
    }


    fun validatePasswords(word: String, confirmWord: String): Boolean {
        var isValid = true
        Log.d("UserVM-VALIDATE_Word", "word: ${word}")
        Log.d("UserVM-VALIDATE_confirmWord", "confirmWord: ${confirmWord}")
        if(word.length == 0){

            _wordRecContrError.value = "La contrasenya no pot esta vuida."
            isValid = false
        }
        else if(confirmWord.length == 0){
            _confirmWordError.value = "La contrasenya no pot esta vuida."
            isValid = false
        }else{
            if(word.equals(confirmWord)){
                _confirmWordError.value = null
                _wordRecContrError.value = null
            }else{
                _confirmWordError.value = "Les contrasenyes no coincideixen."
                _wordRecContrError.value = null
               // _wordRecContrError.value = "Les contrasenyes no coincideixen."
                isValid = false
            }

        }




        return isValid
    }
}