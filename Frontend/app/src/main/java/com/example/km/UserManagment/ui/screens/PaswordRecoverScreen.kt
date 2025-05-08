package com.example.km.UserManagment.ui.screens

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PasswordRecoverScreen(
    loginViewModel: LoginViewModel,
    navController: NavController,
    userViewModel: UserViewModel,
    userState: State<User?>
) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(1) }
    var email by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val ReqUser by userViewModel.user.collectAsState()

    var errorTokenMesage:String? by remember { mutableStateOf(null) }
    var errorEmailMesage:String? by remember { mutableStateOf(null) }
    var errorPasswordMesage:String? by remember { mutableStateOf(null) }


   /* Row(Modifier.fillMaxWidth().background(Color.Black).padding(12.dp)){

        Spacer(Modifier.width(12.dp))
        Text("Recuperar contrasenya", fontSize = 22.sp, color = Color.White)

    }
    Spacer(Modifier.height(12.dp))*/
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Barra de progreso
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val steps = listOf("Email", "Token", "Contraseña")
            steps.forEachIndexed { index, step ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = step,
                        color = if (currentStep == index + 1) Color.Blue else Color.Gray
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (currentStep == index + 1) Color.Blue else Color.Gray,
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        when (currentStep) {
            1 -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Correo electrónico") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { if (email.isNotBlank()){

                                loginViewModel.forgotPassword(
                                    email,
                                    onSuccess = {
                                        Handler(Looper.getMainLooper()).post {
                                            Toast.makeText(
                                                context,
                                                "Correu enviat",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        currentStep = 2
                                                },
                                    onError = {}
                                )

                                currentStep = 2
                            } },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Enviar token")
                        }
                    }
                }
            }

            2 -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = token,
                            onValueChange = { token = it },
                            label = { Text("Token de recuperación") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { if (token.isNotBlank()){
                                Log.d("screenPassRec", "email: $email")
                                userViewModel.findByEmail(email)

                                loginViewModel.verifyToken(email, token,
                                    onSuccess = {
                                        Log.d("PassWdScreen","token: $token")

                                        errorTokenMesage = null
                                        Handler(Looper.getMainLooper()).post {
                                        Toast.makeText(
                                            context,
                                            "Token Correcte",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                        currentStep = 3
                                                },
                                    onError = { errorMessage ->
                                        errorTokenMesage = errorMessage
                                        Log.d("PassWdScreen","token: $token")
                                        Log.d("PassWdScreen","UserRequesterToken: ${ReqUser?.resetToken})")
                                    }

                                )
                            } },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Confirmar")
                        }
                        if (errorTokenMesage!=null) {
                            Card(modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .align(Alignment.CenterHorizontally)
                                .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                                colors = CardColors(
                                    MaterialTheme.colorScheme.error,
                                    Transparent,Color.Transparent, Color.Transparent),

                                ) {
                                Text(
                                    text = errorTokenMesage.toString(),
                                    color =  Color.White,
                                    fontSize = 15.sp,
                                    // fontWeight = FontWeight(500),
                                    style = MaterialTheme.typography.bodySmall,

                                    modifier = Modifier.align(Alignment.CenterHorizontally)
                                )
                            }

                        }
                    }
                }
            }

            3 -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Nueva contraseña") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text("Confirmar contraseña") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                if (newPassword == confirmPassword && newPassword.isNotBlank()) {

                                    // Aquí iría lógica del ViewModel para cambiar la contraseña
                                    ReqUser?.let { userViewModel.updatePasswordUser(it.email, newPassword  ) }
                                    navController.navigate("login")
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Cambiar contraseña")
                        }
                    }
                }
            }
        }
        Row(){
            IconButton(onClick = {
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Tornar enrere",
                    tint = Color.Black
                )
            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun sendEmail(email: String, loginViewModel: LoginViewModel, context: Context, navController: NavController, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        loginViewModel.forgotPassword(email,
            onSuccess = {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Correu enviat",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },

            onError = { errorMessage ->
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun verifyToken(token: String, userViewModel: UserViewModel, loginViewModel: LoginViewModel, context: Context, navController: NavController, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        loginViewModel.loginUser(context, navController,
            onSuccess = {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(
                        context,
                        "Login correcte",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("home")
                }
            },

            onError = { errorMessage ->

            })
    }
}