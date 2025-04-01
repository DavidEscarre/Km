package com.example.km.UserManagment.ui.screens

import android.content.Context
import android.graphics.drawable.Icon
import android.os.Handler
import android.os.Looper
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel,  userState: State<User?>) {


    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val email by loginViewModel.email .collectAsState()
    val word by loginViewModel.word.collectAsState()

    val emailError by loginViewModel.emailError .collectAsState()
    val wordError by loginViewModel.wordError.collectAsState()

    val loginError by loginViewModel.loginError.collectAsState()

   /* val _isError = MutableStateFlow<String>("")
    val isError: StateFlow<String>  = _isError*/
   // var isError by rememberSaveable { mutableStateOf("") }
     // focus
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        //isError = ""

    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { focusManager.clearFocus() } ,
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
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(emailFocusRequester),
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
                ),
                singleLine = true,
                isError = emailError!=null || loginError != null,
                trailingIcon = {
                    if (emailError!=null || loginError != null) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { passwordFocusRequester.requestFocus() } // Pasar al siguiente campo
                ),
            )
            if (emailError!=null) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                    colors = CardColors(MaterialTheme.colorScheme.error,
                        Transparent,Color.Transparent, Color.Transparent),

                    ) {
                    Text(
                        text = emailError.toString(),
                        color =  Color.White,
                        fontSize = 15.sp,
                        //fontWeight = FontWeight(500),
                        style = MaterialTheme.typography.bodySmall,

                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = word,
                onValueChange = {loginViewModel.onPasswordChange(it) },
                label = { Text("Contrasenya", color= Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(passwordFocusRequester),
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
                ),
                singleLine = true,
                isError = wordError!=null || loginError != null,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onLogin(loginViewModel, context, navController, coroutineScope)
                    }
                ),
                trailingIcon = {
                    if (wordError!=null || loginError != null) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }

            )
            if (wordError!=null) {
               Card(modifier = Modifier
                   .fillMaxWidth()
                   .wrapContentHeight()
                   .align(Alignment.CenterHorizontally)
                   .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                   colors = CardColors(MaterialTheme.colorScheme.error,
                       Transparent,Color.Transparent, Color.Transparent),

               ) {
                   Text(
                       text = wordError.toString(),
                       color =  Color.White,
                       fontSize = 15.sp,
                      // fontWeight = FontWeight(500),
                       style = MaterialTheme.typography.bodySmall,

                       modifier = Modifier.align(Alignment.CenterHorizontally)
                   )
               }

            }


            Spacer(modifier = Modifier.height(52.dp))
            if (loginError != null) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally)
                    .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                    colors = CardColors(MaterialTheme.colorScheme.error,
                        Transparent,Color.Transparent, Color.Transparent),

                    ) {
                    Text(
                        text = loginError.toString(),
                        color =  Color.White,
                        fontSize = 15.sp,
                        // fontWeight = FontWeight(500),
                        style = MaterialTheme.typography.bodySmall,

                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onLogin(loginViewModel, context, navController, coroutineScope) },
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


fun onLogin(loginViewModel: LoginViewModel, context: Context, navController: NavController, coroutineScope: CoroutineScope) {
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
