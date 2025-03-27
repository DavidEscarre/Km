package com.example.km.UserManagment.ui.screens

import android.graphics.drawable.Icon
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.User
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel, userState: MutableState<User?>) {


    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val email by loginViewModel.email .collectAsState()
    val word by loginViewModel.word.collectAsState()

    // Reinicia los valores cuando se entra en la pantalla
   LaunchedEffect(Unit) {



    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top= 0.dp, bottom = 20.dp)

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                   // .offset(0.dp,-60.dp)
                    .size(284.dp)
                    .clip(CircleShape)
                    .background(Color.White)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "sombra logo",
                    Modifier

                        .offset(x = (0).dp, y = (5).dp)
                        .blur(4.dp)
                        .size(284.dp, 189.dp),

                    tint = Color(0f,0f,0f,0.5f),
                )
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(254.dp, 189.dp),
                    tint = Color.Black,

                )

            }


            Box(

                contentAlignment = Alignment.Center,
                modifier = Modifier .size(179.dp, 66.dp)//.offset(0.dp,-22.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.kmlogoname),
                    contentDescription = null,
                    modifier = Modifier.size(179.dp, 66.dp),
                    tint = Color.White,

                    )
            }

            Spacer(modifier = Modifier.height(36.dp))
            Text(
                text = "Login",
                style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = White)
            )
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = email,
                onValueChange = {loginViewModel.onEmailChange(it) },
                label = { Text("Email", color= Color.White) },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = word,
                onValueChange = {loginViewModel.onPasswordChange(it) },
                label = { Text("Contrasenya", color= Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Gray,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                )
            )


            Spacer(modifier = Modifier.height(52.dp))

            Button(
                onClick = {
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
                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(
                                        context,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                    }
                      /*  try {
                            val response = AuthRetrofitInstance.authApi.login(email, password)
                            when {
                                response.isSuccessful -> {
                                    val authenticatedUser = response.body()
                                    if (authenticatedUser != null) {
                                        userState.value = authenticatedUser
                                        navController.navigate("categoryScreen")
                                    }
                                }

                                response.code() == 401 -> {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.invalid_credentials),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                response.code() == 400 -> {
                                    Toast.makeText(
                                        context,
                                        context.getString(R.string.user_not_activated),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                else -> {
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: IOException) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.connection_error),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: HttpException) {
                            Toast.makeText(
                                context,
                                context.getString(R.string.server_error, e.code()),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }*/
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Login", color = Color.Black, fontSize = 21.sp, fontWeight = FontWeight(900))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "He ovlidat la contrasenya",
                color = Color(1f,1f,1f,0.7f),
                fontSize = 14.sp,
                modifier = Modifier.clickable { navController.navigate("passwordRecover") }
            )
            Button(
                onClick = {
                    coroutineScope.launch {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(
                                context,
                                "Hola subnormal",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate("home")
                        }
                    }
                }
            ){
                Text("Entrar sin login", color = Color.Black, fontSize = 21.sp, fontWeight = FontWeight(900))

            }
        }
    }


}
