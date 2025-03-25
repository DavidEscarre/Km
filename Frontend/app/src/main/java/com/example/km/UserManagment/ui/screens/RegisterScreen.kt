package com.example.km.UserManagment.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(/*registerViewModel: RegisterViewModel,*/ navController: NavController) {
    val context = LocalContext.current
    /*val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        registerViewModel.selectedImageUri.value = uri
    }

    // Reinicia los valores cuando se entra en la pantalla
    LaunchedEffect(Unit) {
        registerViewModel.name.value = ""
        registerViewModel.email.value = ""
        registerViewModel.phone.value = ""
        registerViewModel.password.value = ""
        registerViewModel.confirmPassword.value = ""
        registerViewModel.selectedImageUri.value = null
    }  */
}

