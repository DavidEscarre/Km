package com.example.km.UserManagment.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.km.UserManagment.data.repositories.UserRepositoryImpl
import com.example.km.core.models.User
import com.example.km.core.utils.base64ToBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
class UserViewModel: ViewModel()  {


    private val userRepo = UserRepositoryImpl()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user

    private val _profileImageBitmap = MutableStateFlow<Bitmap?>(null)
    val profileImageBitmap: StateFlow<Bitmap?> get() = _profileImageBitmap

    fun getProfileImage(user: User){
        user.foto?.let {
            _profileImageBitmap.value = base64ToBitmap(it)
        }

    }

    fun updateUser(userEmail: String, updatedUser: User, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            // actualitzar imatge posantsela al usuari si hi ha
            imageUri?.let { uri ->
                try {
                    // Abrimos el contenido y leemos todos los bytes
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bytes = inputStream?.use { it.readBytes() }
                    inputStream?.close()

                    // Codificamos en Base64 y asignamos al campo foto
                    bytes?.let {
                        val base64String = android.util.Base64.encodeToString(it, android.util.Base64.NO_WRAP)
                        updatedUser.foto = base64String
                    }
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Error leyendo o codificando imagen: ${e.message}")
                }
            }


            try {
                val response = userRepo.update(userEmail, updatedUser)
                if (response.isSuccessful) {

                    updatedUser.foto?.let { _profileImageBitmap.value = base64ToBitmap(it)}
                } else {
                    Log.e("UserViewModel", "Error al actualizar usuario: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Excepción al actualizar usuario: ${e.message}")
            }
        }

    }
  /*  fun uploadProfileImage(userEmail: String, uri: Uri, context: Context) {
        viewModelScope.launch {

            val file = uriToFile(uri, context) // Convierte Uri a archivo
            //val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
           // val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

            try {
                val response =userRepo.update(email)
                if (response.isSuccessful) {
                    profileImageUri.value = uri
                } else {
                    Log.e("UserViewModel", "Error al actualizar imagen")
                }
            } catch (e: Exception) {
                Log.e("UserViewModel", "Excepción: ${e.message}")
            }
        }
    }
    */*/
    private fun uriToFile(uri: Uri, context: Context): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("profile", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }


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