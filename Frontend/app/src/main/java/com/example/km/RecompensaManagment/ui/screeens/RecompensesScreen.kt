package com.example.km.RecompensaManagment.ui.screeens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.km.R
import com.example.km.RutaManagment.ui.viewmodels.RecompensaViewModel
import com.example.km.core.models.Recompensa
import com.example.km.core.models.User
import com.example.km.core.utils.enums.EstatRecompensa
import com.example.km.navigation.BottomNavigationBar
import com.example.km.navigation.TopBar
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecompensesScreen(
    navController: NavController,
    userState: State<User?>,
    recompensaViewModel: RecompensaViewModel

) {
    val scrollState = rememberScrollState()
    val user: User? = userState.value
    val recompensas by recompensaViewModel.recompensas.collectAsState()

    LaunchedEffect(Unit) {
        if (user != null) {
            Log.d("RecompensaScreen", "fetching ....")
            recompensaViewModel.fetchAllRecompensas(user.email)
        }
    }



    Scaffold(
        topBar = { TopBar("Recompenses", userState.value, navController)},
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            items(recompensas.reversed()) { recompensa ->
                RecompensaItem(recompensa,navController)
            }
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecompensaItem(recompensa: Recompensa, navController: NavController) {
    // Formatter per a dates dd/MM/yyyy
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    // Funció per formatejar si existeix; admet ISO date o ISO date-time
    fun formatDate(dateString: String?): String? = dateString?.let { str ->
        try {
            // Si ve amb hora, parseja com a LocalDateTime
            val date = if (str.contains("T")) {
                LocalDateTime.parse(str).toLocalDate()
            } else {
                LocalDate.parse(str)
            }
            date.format(formatter)
        } catch (e: Exception) {
            // En cas d'error, retorna el text original
            str
        }
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A),
            contentColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                 navController.navigate("recompensa_details/${recompensa.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Nom i data de creació
            Text(
                text = recompensa.nom,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Creat el: ${formatDate(recompensa.dataCreacio)}",
                color = Color.White
            )

            // Dates opcionals formatejades
            recompensa.dataReserva?.let {
                Text(text = "Reservat el: ${formatDate(it)}", color = Color.White)
            }
            recompensa.dataAssignacio?.let {
                Text(text = "Assignat el: ${formatDate(it)}", color = Color.White)
            }
            recompensa.dataRecollida?.let {
                Text(text = "Recollit el: ${formatDate(it)}", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Punt de bescanvi
            Text(text = "Punt de bescanvi: ${recompensa.puntBescanvi}", color = Color.White)

            // Punts associats en estil Card amb icona
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2ECC71),
                    contentColor = Color.White
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${recompensa.preu.toInt()}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(R.drawable.coins),
                        contentDescription = "Punts",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Estat de la recompensa
            val estatText = when (recompensa.estat) {
                EstatRecompensa.DISPONIBLE -> "Disponible"
                EstatRecompensa.RESERVADA -> "Reservada"
                EstatRecompensa.ASSIGNADA -> "Assignada"
                EstatRecompensa.RECOLLIDA -> "Recollida"
            }
            Button(
                onClick = { /* Opcional: acció segons estat */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (recompensa.estat) {
                        EstatRecompensa.DISPONIBLE -> Color(0xFF2ECC71)
                        EstatRecompensa.RESERVADA -> Color(0xFFF1C40F)
                        EstatRecompensa.ASSIGNADA -> Color(0xFF3498DB)
                        EstatRecompensa.RECOLLIDA -> Color(0xFFE74C3C)
                    },
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = estatText)
            }

            // Nom de l'usuari si no està disponible
            if (recompensa.estat != EstatRecompensa.DISPONIBLE) {
                recompensa.ciclista?.nom?.let { nom ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Ciclista: $nom", color = Color.White)
                }
            }
        }
    }
}
