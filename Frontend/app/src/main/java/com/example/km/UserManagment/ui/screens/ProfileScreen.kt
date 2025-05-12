package com.example.km.UserManagment.ui.screens

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.key.Key.Companion.A
import androidx.compose.ui.input.key.Key.Companion.W
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RecompensaViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.Recompensa
import com.example.km.core.models.Ruta
import com.example.km.core.models.User
import com.example.km.core.ui.theme.SoftBlack
import com.example.km.core.utils.base64ToBitmap
import com.example.km.core.utils.enums.EstatRecompensa
import com.example.km.core.utils.enums.EstatRuta
import com.example.km.core.utils.formatDate
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(rutaViewModel: RutaViewModel, recompensaViewModel: RecompensaViewModel, context: Context,loginViewModel: LoginViewModel, navController: NavController, userState: State<User?>) {
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val user = userState.value
    val imageBitmap = user?.foto?.let { base64ToBitmap(it) }

    val historialRecompensas by recompensaViewModel.recompensasUser.collectAsState()
    val historialRutas by rutaViewModel.rutas.collectAsState()

    LaunchedEffect(Unit) {
        if(user != null){
            recompensaViewModel.fetchAllUserRecompensas(user.email)
            rutaViewModel.fetchAllRutas(user.email)
        }


    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text("Menu",Modifier.align(Alignment.CenterHorizontally), fontSize = 34.sp, fontWeight = FontWeight(900), color = Color(0xFF3E3E3E))
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(modifier = Modifier.width(300.dp).align(Alignment.CenterHorizontally),1.dp, Color(0xFF3E3E3E), )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth().clickable
                  { loginViewModel.logOut(context, navController) }
                      .background(Color(0xFF2b2b2b)).align(Alignment.CenterHorizontally),
                      Arrangement.Center

                      ) {
                        Text("Logout", color = Color.White, fontSize = 23.sp, fontWeight = FontWeight(700))
                    }

                }
            }
        }
    ) {
        Scaffold(
            topBar = { TopBar("Perfil", userState.value, navController)},
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(scrollState)
                    .padding(paddingValues)
            ) {
                Spacer(Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 11.dp, bottom = 0.dp, top = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color(0xFF3E3E3E)
                        )
                    }


                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Imatge de perfil.",
                            modifier = Modifier
                                .size(200.dp)
                                .border(BorderStroke(1.dp,Color.Black), shape =CircleShape )
                                .clip(CircleShape)
                                .clickable {  },
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.perfilimagedefault),
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .clickable {   }
                        )
                    }

                }
                HorizontalDivider(Modifier.padding(16.dp), thickness = 2.dp )

                Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center){
                    Text(user?.nom?: "Nom Usuari",Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 36.sp, fontWeight = FontWeight.Bold)
                }
                Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center){
                    Text(user?.email?: "user.@gmail.com",Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 23.sp, fontWeight = FontWeight.W600)
                }
                Row(){
                    Text("Rol: ${user?.rol}", Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 19.sp, fontWeight = FontWeight.W500)
                }
                HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 2.dp )

                Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center){
                    Text("Observacions",Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 23.sp, fontWeight = FontWeight.W600)
                }

                Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center){
                    Text(user?.observacions?: "observacions d'usuari",Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 21.sp)
                }
                HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 2.dp )

                Row(){
                    Text("Adreça: ${user?.adreca}", Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 19.sp, fontWeight = FontWeight.W500)
                }
                Row(){
                    Text("Telèfon: ${user?.telefon}", Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 19.sp, fontWeight = FontWeight.W500)
                }
                Row(){
                    Text("Data alta: ${user?.dataAlta?.toLocalDate()}", Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 19.sp, fontWeight = FontWeight.W500)
                }

                HorizontalDivider(Modifier.padding(vertical = 5.dp, horizontal = 16.dp), thickness = 2.dp )

                Row(Modifier.align(Alignment.CenterHorizontally), horizontalArrangement = Arrangement.Center){
                    Text("Historial",Modifier.padding(horizontal = 16.dp) ,textAlign = TextAlign.Center, fontSize = 23.sp, fontWeight = FontWeight.W600)
                }

                HorizontalDivider(Modifier.padding(vertical = 5.dp, horizontal = 16.dp), thickness = 2.dp )
                var selectedTab by remember { mutableStateOf(0) }
                val tabTitles = listOf("Rutes", "Recompenses")

                Spacer(modifier = Modifier.height(12.dp))

// Tabs
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    tabTitles.forEachIndexed { index, title ->
                        Button(
                            onClick = { selectedTab = index },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedTab == index) Color(0xFF3E3E3E) else Color.LightGray
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

// Content
                when (selectedTab) {
                    0 -> {
                        // Historial de Rutas
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            if (!historialRutas.isNullOrEmpty()) {
                                historialRutas?.forEach { ruta ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        elevation = CardDefaults.cardElevation(4.dp),
                                        colors = CardColors(SoftBlack, Color.White,SoftBlack,SoftBlack),
                                        onClick = {navController.navigate("ruta_details/${ruta.id}")}
                                    ) {
                                        Text(buildAnnotatedString {
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 20.sp
                                            )) { append("Data inici: ") }
                                            withStyle(style = SpanStyle(
                                                fontWeight = FontWeight.Normal,
                                                fontFamily = FontFamily.Monospace,
                                                fontSize = 19.sp
                                            )) { append(ruta.dataInici.toLocalDate().toString()) }
                                        }, textAlign = TextAlign.Center, lineHeight = 32.sp, modifier = Modifier.align(Alignment.Start).padding(start = 12.dp, top= 12.dp))

                                        Box(Modifier.fillMaxSize()){
                                            Column(Modifier.padding(12.dp).align(AbsoluteAlignment.CenterLeft)) {

                                                Text(buildAnnotatedString {
                                                    withStyle(style = SpanStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )) { append("Distància: ") }
                                                    withStyle(style = SpanStyle(
                                                        fontWeight = FontWeight.Normal,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 16.sp
                                                    )) { append(ruta.distancia.toString()) }
                                                    withStyle(style = SpanStyle(
                                                        fontWeight = FontWeight.Normal,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 6.sp
                                                    )) { append(" ") }
                                                    withStyle(style = SpanStyle(

                                                        fontWeight = FontWeight.Normal,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 16.sp
                                                    )) { append("Km") }
                                                }, textAlign = TextAlign.Center, lineHeight = 32.sp)

                                                Text(buildAnnotatedString {
                                                    withStyle(style = SpanStyle(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )) { append("Durada: ") }
                                                    withStyle(style = SpanStyle(
                                                        fontWeight = FontWeight.Normal,
                                                        fontFamily = FontFamily.Monospace,
                                                        fontSize = 16.sp
                                                    )) { append(ruta.durada) }
                                                }, textAlign = TextAlign.Center, lineHeight = 32.sp)
                                            }

                                            SaldoRutaCard(Modifier.align(Alignment.TopEnd).offset(x= (-16).dp, y= (-30).dp),ruta)

                                            Row(Modifier.align(Alignment.CenterEnd).padding(16.dp)){

                                                Text("Estat: ", Modifier.align(Alignment.CenterVertically).padding(end = 8.dp),
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                EstatRutaCard(Modifier.size(110.dp, 26.dp).padding(0.dp).align(Alignment.CenterVertically), ruta)
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text("No hi ha rutes registrades.", modifier = Modifier.padding(8.dp))
                            }
                        }
                    }

                    1 -> {
                        // Historial de Recompensas
                        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                            if (!historialRecompensas.isNullOrEmpty()) {
                                historialRecompensas.forEach { recompensa ->
                                    RecompensaCard(
                                        recompensa = recompensa,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            } else {
                                Text(
                                    "No hi ha recompenses registrades.",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 16.sp,
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }

                    }
                }
            }
        }

    }

}
@Composable
fun EstatRecompensaCard(modifier: Modifier, recompensa: Recompensa){
    val reservada = recompensa?.estat == EstatRecompensa.RESERVADA
    val assignada = recompensa.estat == EstatRecompensa.ASSIGNADA
    val recollida = recompensa?.estat == EstatRecompensa.RECOLLIDA

    val container = if (assignada) Color(0xFF2ECC71) else {
        if(reservada) Color(0xFFFCFCFC) else Color.Gray
    }
    val content =  if(reservada || recollida) Color.Black else Color.White
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =  container,
            contentColor = content
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text =  if(assignada) "Assignada" else if(reservada) "Reservada" else if(recollida) "Recollida" else "Disponible",
            color = if(reservada || recollida) Color.Black else Color.White
        )
    }
}

@Composable
fun SaldoRecompensaCard(modifier: Modifier, recompensa: Recompensa){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =  Color.Black,
            contentColor = if ((recompensa.preu > 0) && recompensa.estat.equals(EstatRecompensa.ASSIGNADA)) Color(0xFFE74C3C)
            else Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {

            Text(
                text = "${if (recompensa.estat.equals(EstatRecompensa.ASSIGNADA)) "-" else ""}${recompensa.preu}",
                color =  if ((recompensa.preu > 0) && recompensa.estat.equals(EstatRecompensa.ASSIGNADA)) Color(0xFFE74C3C)
                else Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                painter = painterResource(R.drawable.coins),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}
@Composable
fun EstatRutaCard(modifier: Modifier, ruta: Ruta){
    val isPendent = ruta?.estat == EstatRuta.PENDENT
    val isValidada = ruta.estat == EstatRuta.VALIDA
    val container = if (isValidada && !isPendent) Color(0xFF2ECC71) else {
        if(!isValidada && !isPendent) Color(0xFFE74C3C) else Color.Gray
    }
    val content = Color.White
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =  container,
            contentColor = container
        )
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text =  if(isValidada && !isPendent) "Validada" else if(!isValidada && !isPendent) "No validada" else "Pendent",
            color = Color.White
        )
    }
}

@Composable
fun SaldoRutaCard(modifier: Modifier, ruta: Ruta){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor =  Color.Black,
            contentColor = if ((ruta.saldo >= 0) && ruta.estat.equals(EstatRuta.VALIDA)) Color(0xFF72F774)
            else Color.White
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {

            Text(
                text = "${if (ruta.saldo >= 0) "+" else "-"}${ruta.saldo}",
                color = if ((ruta.saldo >= 0) && ruta.estat.equals(EstatRuta.VALIDA)) Color(0xFF72F774)
                else Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                painter = painterResource(R.drawable.coins),
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecompensaCard(
    recompensa: Recompensa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = SoftBlack,
            contentColor = Color.White
        )
    ) {
        Box(Modifier.fillMaxSize()){
            Column(modifier = Modifier.padding(12.dp).align(AbsoluteAlignment.CenterLeft)) {
                // Títol
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 21.sp)) {
                        append("Títol: ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 21.sp, )) {
                        append(recompensa.nom)
                    }
                }, lineHeight = 28.sp)
                Spacer(Modifier.height(12.dp))
                // Descripció
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp)) {
                        append("Descripció: ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)) {
                        append(recompensa.descripcio)
                    }
                }, lineHeight = 24.sp, modifier = Modifier.padding(top = 4.dp))

                // Observacions
                Text(buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 19.sp)) {
                        append("Observacions: ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)) {
                        append(recompensa.observacions ?: "-")
                    }
                }, lineHeight = 24.sp, modifier = Modifier.padding(top = 4.dp))

                // Dates
                recompensa.dataReserva?.let{
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                            append("Data reserva: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, fontFamily = FontFamily.Monospace)) {
                            append(formatDate(it))
                        }
                    }, lineHeight = 24.sp, modifier = Modifier.padding(top = 4.dp))

                }

                recompensa.dataAssignacio?.let {
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                            append("Data assignació: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, fontFamily = FontFamily.Monospace)) {
                            append(formatDate(it))
                        }
                    }, lineHeight = 24.sp, modifier = Modifier.padding(top = 4.dp))
                }


                recompensa.dataRecollida?.let {
                    Text(buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)) {
                            append("Data recollida: ")
                        }
                        withStyle(SpanStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp, fontFamily = FontFamily.Monospace)) {
                            append(formatDate(it))
                        }
                    }, lineHeight = 24.sp, modifier = Modifier.padding(top = 4.dp))
                }
                Row(Modifier.align(Alignment.CenterHorizontally).padding(16.dp)){

                    Text("Estat: ", Modifier.align(Alignment.CenterVertically).padding(end = 8.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    EstatRecompensaCard(Modifier.size(110.dp, 26.dp).padding(0.dp).align(Alignment.CenterVertically), recompensa)
                }
            }
            //Preu
            SaldoRecompensaCard((Modifier.align(Alignment.TopEnd).offset(x= (-12).dp, y= (10).dp)), recompensa)
            // Estat

        }

    }
}
