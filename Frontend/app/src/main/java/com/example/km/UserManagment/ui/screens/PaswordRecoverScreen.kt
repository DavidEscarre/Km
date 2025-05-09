package com.example.km.UserManagment.ui.screens

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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

    val emailError by loginViewModel.emailRecContrError.collectAsState()
    val wordError by loginViewModel.wordRecContrError.collectAsState()
    val confirmWordError by loginViewModel.confirmWordError.collectAsState()
    val tokenError by loginViewModel.tokenError.collectAsState()

    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }
    val tokenFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    val RecContrError by loginViewModel.RecContrError.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .clickable { focusManager.clearFocus() },
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, bottom = 0.dp, top = 32.dp) // Espaciado desde el borde
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Tornar enrere",
                tint = Color.White
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 58.dp, bottom = 42.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    // .offset(0.dp,-60.dp)
                    .size(176.dp)
                    .clip(CircleShape)
                    .background(Color.White)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "sombra logo",
                    Modifier
                        .offset(x = (0).dp, y = (5).dp)
                        .blur(4.dp)
                        .size(176.dp, 131.dp),
                    tint = Color(0f, 0f, 0f, 0.5f),
                )

                Icon(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(176.dp, 131.dp),
                    tint = Color.Black)
            }
            Spacer(Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp, 44.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.kmlogoname),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp, 44.dp),
                    tint = Color.White)
            }
            Spacer(Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Barra de progreso
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp, top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                   // Spacer(Modifier.width(12.dp))
                    BikeProgressBar(currentStep)

                  /*  val steps = listOf("Email", "Token", "Contraseña")
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
                    }*/
                }

                when (currentStep) {
                    1 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardColors(Color.Transparent, Color.Transparent, Color.Transparent ,Color.Transparent)
                        ) {
                            Column(modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Introdueix el teu correu electronic per rebre el token de recuperació.",
                                    modifier = Modifier.padding(bottom = 42.dp),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W400,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )

                                OutlinedTextField(
                                    value = email,
                                    onValueChange = {
                                        email = it
                                        loginViewModel.onEmailRecContrChange(it) },
                                    label = { Text("Email", color = Color.White) },
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
                                    isError = emailError != null || RecContrError != null,
                                    trailingIcon = {
                                        if (emailError != null) {
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
                                if (emailError != null) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .align(Alignment.CenterHorizontally)
                                            .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                                        colors = CardColors(
                                            MaterialTheme.colorScheme.error,
                                            Transparent, Color.Transparent, Color.Transparent
                                        ),

                                        ) {
                                        Text(
                                            text = emailError.toString(),
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            //fontWeight = FontWeight(500),
                                            style = MaterialTheme.typography.bodySmall,

                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Button(
                                    onClick = {
                                        if (email.isNotBlank()) {
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
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                                ) {
                                    Text(
                                        "Enviar token",
                                        color = Color.Black,
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight(900)
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardColors(Color.Transparent, Color.Transparent, Color.Transparent ,Color.Transparent)

                        ) {
                            Column(modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Introdueix el token de recuperació que se li ha enviat al correu electrònic.",                                    modifier = Modifier.padding(bottom = 12.dp),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W400,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                                Text(
                                        "Pot trigar una mica en arribar.",
                                modifier = Modifier.padding(bottom = 42.dp),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W400,
                                textAlign = TextAlign.Center,
                                color = Color.White
                                )
                                OutlinedTextField(
                                    value = token,
                                    onValueChange = {

                                        token = it
                                        loginViewModel.onTokenChange(it) },
                                    label = { Text("Token", color = Color.White) },
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
                                    isError = tokenError != null || RecContrError != null,
                                    trailingIcon = {
                                        if (tokenError != null) {
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
                                if (tokenError != null) {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .align(Alignment.CenterHorizontally)
                                            .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                                        colors = CardColors(
                                            MaterialTheme.colorScheme.error,
                                            Transparent, Color.Transparent, Color.Transparent
                                        ),

                                        ) {
                                        Text(
                                            text = tokenError.toString(),
                                            color = Color.White,
                                            fontSize = 15.sp,
                                            // fontWeight = FontWeight(500),
                                            style = MaterialTheme.typography.bodySmall,

                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(32.dp))

                                Button(
                                    onClick = {
                                        if (token.isNotBlank()) {
                                            Log.d("screenPassRec", "email: $email")
                                            userViewModel.findByEmail(email)

                                            loginViewModel.verifyToken(email, token,
                                                onSuccess = {
                                                    Log.d("screenPassRec", "token: $token")
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
                                                    Log.d("screenPassRec", "token: $token")
                                                    Log.d(
                                                        "screenPassRec",
                                                        "UserRequesterToken: ${ReqUser?.resetToken})"
                                                    )
                                                }

                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth().align(Alignment.End),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                                ) {
                                    Text(
                                        "Confirmar",
                                        color = Color.Black,
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight(900)
                                    )
                                }
                            }
                        }
                    }

                    3 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardColors(Color.Transparent, Color.Transparent, Color.Transparent ,Color.Transparent)

                        ) {
                            Column(modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Crea una contrasenya nova per al teu compte",
                                    modifier = Modifier.padding(bottom = 42.dp),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W400,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                                OutlinedTextField(
                                    value = newPassword,
                                    onValueChange = {
                                        newPassword = it
                                        loginViewModel.onPasswordRecContrChange(
                                            it,
                                            confirmPassword
                                        )
                                    },
                                    label = { Text("Nova contrasenya", color = Color.White) },
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
                                    isError = wordError != null || RecContrError != null,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    trailingIcon = {
                                        if (wordError != null || RecContrError != null) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "Error",
                                                tint = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                    OutlinedTextField(
                                        value = confirmPassword,
                                        onValueChange = {
                                            confirmPassword = it
                                            loginViewModel.onPasswordRecContrChange(
                                                newPassword,
                                                it
                                            )
                                        },
                                        label = {
                                            Text(
                                                "Confirmar contrasenya",
                                                color = Color.White
                                            )
                                        },
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
                                        isError = confirmWordError != null || RecContrError != null,
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            imeAction = ImeAction.Done
                                        ),
                                        keyboardActions = KeyboardActions(
                                            onDone = {
                                                focusManager.clearFocus()

                                            }
                                        ),
                                        trailingIcon = {
                                            if (confirmWordError != null || RecContrError != null) {
                                                Icon(
                                                    imageVector = Icons.Default.Warning,
                                                    contentDescription = "Error",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }

                                    )
                                    if (confirmWordError != null) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .align(Alignment.CenterHorizontally)
                                                .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                                            colors = CardColors(
                                                MaterialTheme.colorScheme.error,
                                                Transparent, Color.Transparent, Color.Transparent
                                            ),

                                            ) {
                                            Text(
                                                text = confirmWordError.toString(),
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                // fontWeight = FontWeight(500),
                                                style = MaterialTheme.typography.bodySmall,

                                                modifier = Modifier.align(Alignment.CenterHorizontally)
                                            )
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(32.dp))

                                    Button(
                                        onClick = {
                                            if (wordError == null && confirmWordError == null && RecContrError == null) {
                                                Log.d("screenPassRec", "email: $email")
                                                userViewModel.findByEmail(email)
                                                ReqUser?.let {
                                                    userViewModel.updatePasswordUser(
                                                        it.email,
                                                        newPassword
                                                    )
                                                }
                                                currentStep = 4
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth().align(Alignment.End),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                                    ) {
                                        Text(
                                            "Cambiar contraseña",
                                            color = Color.Black,
                                            fontSize = 21.sp,
                                            fontWeight = FontWeight(900)
                                        )
                                    }

                            }
                        }
                    }
                    4 -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardColors(Color.Transparent, Color.Transparent, Color.Transparent ,Color.Transparent)
                        ) {
                            Column(modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Contrasenya Actualitzada correctament.",
                                    modifier = Modifier.padding(bottom = 42.dp),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.W800,
                                    textAlign = TextAlign.Center,
                                    color = Color.White
                                )
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .shadow(
                                            elevation = 24.dp,
                                            shape = CircleShape,
                                            ambientColor = Color(0xFF00FF00), // verde brillante
                                            spotColor = Color(0xFF00FF00)
                                        )
                                        .background(Color.Black, shape = CircleShape), // Fondo opcional para contraste
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.success),
                                        contentDescription = "Correcte",
                                        modifier = Modifier.size(48.dp),
                                        colorFilter = ColorFilter.tint(Color(0xFF00FF00)) // Tinte verde al icono
                                    )
                                }


                                Spacer(modifier = Modifier.height(32.dp))
                                Button(
                                    onClick = {
                                        navController.navigate("login")
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                                ) {
                                    Text(
                                        "Iniciar sessio",
                                        color = Color.Black,
                                        fontSize = 21.sp,
                                        fontWeight = FontWeight(900)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
                if (RecContrError != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterHorizontally)
                            .padding(start = 26.dp, end = 26.dp, top = 8.dp),
                        colors = CardColors(
                            MaterialTheme.colorScheme.error,
                            Transparent, Color.Transparent, Color.Transparent
                        ),

                        ) {
                        Text(
                            text = RecContrError.toString(),
                            color = Color.White,
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
}

@Composable
fun BikeProgressBar(currentStep: Int) {
    val steps = listOf("Enviar email", "Token", "Contraseña")
    val activeIndex = currentStep.coerceIn(1, steps.size) - 1
    val stepWidth = 80.dp
    if(currentStep == 4){
        val indicatorOffset by animateDpAsState(
            targetValue = (4 * stepWidth) + 16.dp,
            animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
        )
    }
    val indicatorOffset by animateDpAsState(
        targetValue = if(currentStep == 4) (3 * stepWidth) else (activeIndex * stepWidth) + 16.dp,
        animationSpec = tween(durationMillis = 500, easing = LinearOutSlowInEasing)
    )

    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp)) {
        // Fondo con líneas de pasos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            steps.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .width(stepWidth)
                        .height(4.dp)
                        .background(
                            if (index <= activeIndex) Color(0xFFFFFFFF) else Color(0xFF5c5b5b),
                            shape = RoundedCornerShape(2.dp),

                        )
                )
            }
        }

        // Indicador animado
        Row(
            modifier = Modifier
                .offset(x = indicatorOffset, y= -45.dp)
                .size(60.dp)
                //.background(Color.White, shape = CircleShape)
                //.border(2.dp, Color(0xFF0077CC), shape = CircleShape),
        ) {
            Icon(
                painter = painterResource(R.drawable.athletic_bicycle_bike_exercise_game_sport_training_icon_127167) , // Cámbialo por tu bici personalizada
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.padding(2.dp).fillMaxSize().offset(x=8.dp)
            )
        }

        // Etiquetas debajo
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 0.dp, end = 0.dp, top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,

        ) {
            steps.forEachIndexed { index, label ->
                Text(
                    text = label,
                    fontSize = 13.sp,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (index <= activeIndex)  Color(0xFFFFFFFF) else Color(0xFF5c5b5b),
                )
            }
        }
    }
}
