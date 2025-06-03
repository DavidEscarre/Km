package com.example.km.UserManagment.domain.usecases

import android.util.Log
import com.example.km.UserManagment.domain.repositories.LoginRepository
import com.example.km.core.models.User
import retrofit2.Response

class LoginUseCase(private val loginRepo: LoginRepository) {

    suspend operator fun invoke(email: String, word: String): Response<User?> {


        val response =loginRepo.loginUser(email, word)
        return response
    }

  /*  fun validateFields(email: String, word: String): Boolean {
        var isValid = true
        Log.d("VALIDATE_FIELDS", "EMAIL: ${email}")
        Log.d("VALIDATE_FIELDS", "word: ${word}")

        /*//Esta desavilitat per a proves

        if(!email.value.matches(emailRegex)){
            emailError.value = "Correu electronic inválid."
            isValid = false
        }else  emailError.value = null*/

        if(word.value.length == 0){
            _wordError.value = "La contrasenya no pot esta vuida"
            isValid = false
        }else  _wordError.value = null

        return isValid
    }
    */
}