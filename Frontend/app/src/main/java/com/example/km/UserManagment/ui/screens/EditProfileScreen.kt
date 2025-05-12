package com.example.km.UserManagment.ui.screens

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RecompensaViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.UserManagment.ui.viewmodels.UserViewModel
import com.example.km.core.models.User
import com.example.km.core.ui.theme.SoftBlack
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditProfileScreen(
    userViewModel: UserViewModel,
    rutaViewModel: RutaViewModel,
    recompensaViewModel: RecompensaViewModel,
    context: Context,
    loginViewModel: LoginViewModel,
    navController: NavController,
    userState: State<User?>
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val user = userState.value
    val imageBitmap by userViewModel.profileImageBitmap.collectAsState()
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val context = LocalContext.current
    var showChangeImageDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            user?.let { u ->
                userViewModel.updateUser(u.email, u, it, context)
                userViewModel.getProfileImage(u)
            }
            Toast.makeText(context, "Imagen actualizada", Toast.LENGTH_SHORT).show()
        }
    }


    var name by remember { mutableStateOf(user?.nom ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var phone by remember { mutableStateOf(user?.telefon ?: "") }
    var address by remember { mutableStateOf(user?.adreca ?: "") }
    var observations by remember { mutableStateOf(user?.observacions ?: "") }

    var nameError: String? by remember { mutableStateOf( null) }
    var emailError: String? by remember { mutableStateOf(null) }
    var phoneError: String? by remember { mutableStateOf( null) }
    var addressError: String? by remember { mutableStateOf( null) }
    var observationsError: String? by remember { mutableStateOf( null) }

    var validFields = (nameError == null && emailError == null && phoneError == null
            && addressError == null && observationsError == null)

    LaunchedEffect(Unit) {
        user?.let {
            recompensaViewModel.fetchAllUserRecompensas(it.email)
            rutaViewModel.fetchAllRutas(it.email)
            userViewModel.getProfileImage(it)
        }
    }

    Scaffold(
        topBar = { TopBar("Editar perfil", user, navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .background(Color.White)
                .padding(16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                if (imageBitmap != null) {
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clickable { showChangeImageDialog = true },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.perfilimagedefault),
                        contentDescription = "Imagen de perfil por defecto",
                        modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .clickable { showChangeImageDialog = true },
                        contentScale = ContentScale.Crop
                    )
                }

                if (showChangeImageDialog) {
                    ChangeImageDialog(
                        onDismiss = { showChangeImageDialog = false },
                        onConfirm = {
                            launcher.launch("image/*")
                            showChangeImageDialog = false
                        }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(user?.email?: "user.@gmail.com",Modifier.align(Alignment.CenterHorizontally).padding(horizontal = 10.dp) ,textAlign = TextAlign.Center, fontSize = 42.sp, fontWeight = FontWeight.W900)

            HorizontalDivider(Modifier.padding(16.dp), thickness = 2.dp )


            OutlinedTextField(
                value = name,
                isError = nameError!=null,
                maxLines = 1,
                onValueChange = {
                    name = it
                    if(name.length>=30){
                        nameError = "El nom te mes de 30 caracters."
                    }else{
                        nameError = null
                    }
                                },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp)
            )
            if(nameError!=null){
                Card(Modifier.fillMaxSize().padding(horizontal = 16.dp), colors = CardColors(Color(0xFFf55858),Color.White,Color(0xFFf55858),Color(0xFFf55858))){
                    Text(nameError.toString(),Modifier.padding(horizontal = 16.dp))
                }
            }

            OutlinedTextField(
                value = phone,
                isError = phoneError!=null,
                maxLines = 1,
                onValueChange = {
                    phone = it
                   if(!phone.matches("^[0-9]{9}$".toRegex())){
                       phoneError = "Telèfon incorrecte ha de tenir 9 digits numerics."
                   }else{
                       phoneError = null
                   }
                                },
                label = { Text("Telèfon") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp)
            )
            if(phoneError!=null){
                Card(Modifier.fillMaxSize().padding(horizontal = 16.dp), colors = CardColors(Color(0xFFf55858),Color.White,Color(0xFFf55858),Color(0xFFf55858))){
                    Text(phoneError.toString(),Modifier.padding(horizontal = 16.dp))
                }
            }

            OutlinedTextField(
                value = address,
                isError = addressError!=null,
                onValueChange = {
                    address = it
                    if(address.length >= 100){
                        addressError = "Adreça incorrecte, te mes de 100 caracters."
                    }else{
                        addressError = null
                    }
                                },

                label = { Text("Adreça") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 16.dp)

            )
            if(addressError!=null){
                Card(Modifier.fillMaxSize().padding(horizontal = 16.dp), colors = CardColors(Color(0xFFf55858),Color.White,Color(0xFFf55858),Color(0xFFf55858))){
                    Text(addressError.toString(),Modifier.padding(horizontal = 16.dp))
                }
            }

            OutlinedTextField(
                value = observations,
                isError = observationsError!=null,
                onValueChange = {
                    observations = it
                    if(observations.length >= 500){
                        observationsError = "Les observacions tenen, mes de 500 caracters."
                    }else{
                        observationsError = null
                    }
                                },
                label = { Text("Observacions") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                maxLines = 4
            )
            if(observationsError!=null){
                Card(Modifier.fillMaxSize().padding(horizontal = 16.dp), colors = CardColors(Color(0xFFf55858),Color.White,Color(0xFFf55858),Color(0xFFf55858))){
                    Text(observationsError.toString(),Modifier.padding(horizontal = 16.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    user?.let {

                        var updatedUser = it
                        updatedUser.nom = name
                        updatedUser.telefon = phone
                        updatedUser.adreca = address
                        updatedUser.observacions = observations

                        userViewModel.updateUser(email, updatedUser, selectedImageUri, context)
                        Toast.makeText(context, "Perfil actualitzat", Toast.LENGTH_SHORT).show()
                        navController.navigate("profile")
                    }
                },
                enabled = validFields,
                colors = ButtonColors(SoftBlack, Color.White, Color(0xFFb0b0b0), Color(0xFFebe8e8)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text("Guardar canvis",Modifier, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = {
                    navController.navigate("profile") // Vuelve a pantalla anterior
                },
                colors = ButtonColors(Color(0xFFf55858), Color.White, Color(0xFFf55858), Color(0xFFf55858)),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp)
            ) {
                Text("Cancelar",Modifier, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Data alta: ${user?.dataAlta?.toLocalDate() ?: "-"}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        }
    }
}
