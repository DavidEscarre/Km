package com.example.km.navigation

import android.content.pm.verify.domain.DomainVerificationUserState
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.R
import com.example.km.RecompensaManagment.ui.screeens.RecompensesScreen
import com.example.km.RutaManagment.ui.screens.RutaDetailsScreen
import com.example.km.RutaManagment.ui.screens.RutesScreen
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.SistemaManagment.ui.viewmodels.SistemaViewModel
import com.example.km.UserManagment.ui.screens.LoginScreen
import com.example.km.UserManagment.ui.screens.ProfileScreen
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.User
import com.example.km.main.screens.MainScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(sistemaViewModel: SistemaViewModel, puntGPSViewModel: PuntGPSViewModel, rutaViewModel: RutaViewModel, navController: NavController, loginViewModel: LoginViewModel, userState: State<User?>) {
    val navController = rememberNavController()
    val context = LocalContext.current
   // val rutaViewModel: RutaViewModel = viewModel()
    //val puntGPSViewModel: PuntGPSViewModel = viewModel()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel, userState ) }


        // Pantalla Home amb Bottom Navigation
        composable("home") { MainScreen(sistemaViewModel, rutaViewModel, puntGPSViewModel, navController, userState) }

        composable("rutes") { RutesScreen(rutaViewModel, puntGPSViewModel, navController, userState) }
        composable("recompenses") { RecompensesScreen(navController, userState) }
        composable("profile") { ProfileScreen(context, loginViewModel, navController, userState) }

        composable(
            "ruta_details/{rutaId}",
            arguments = listOf(navArgument("rutaId") { type = NavType.LongType })
        ) { backStackEntry ->
            val rutaId = backStackEntry.arguments?.getLong("rutaId")
            rutaId?.let {
                RutaDetailsScreen(
                    rutaId = it,
                    rutaViewModel = rutaViewModel,
                    puntGPSViewModel = puntGPSViewModel,
                    navController = navController,
                    userState = userState
                )
            }
        }

        /*
        composable("categories") { CategoriesScreen(navController) }
        composable("items") { ItemsScreen(navController) }
        composable("profile") { UserProfileScreen(navController) }

        // Detalls de categoria i ítem
        composable("category_detail/{categoryId}") { backStackEntry ->
            CategoryDetailScreen(categoryId = backStackEntry.arguments?.getString("categoryId"))
        }
        composable("item_detail/{itemId}") { backStackEntry ->
            ItemDetailScreen(itemId = backStackEntry.arguments?.getString("itemId"))
        }*/
    }




}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentDestination?.route
    val selectedColor = Color(0xFF3E3E3E)
    val unselectedColor = Color.White

    NavigationBar(containerColor = Color(0xFF3E3E3E), modifier = Modifier.height(90.dp)) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    painter = painterResource(if (currentRoute == "home") R.drawable.map1_contourn else R.drawable.map1_contourn),
                    contentDescription = "home.",
                    tint = if (currentRoute == "home") selectedColor else unselectedColor,
                    modifier = Modifier.size(38.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "rutes",
            onClick = { navController.navigate("rutes") },
            icon = {
                Icon(
                    painter = painterResource(if (currentRoute == "rutes") R.drawable.routes_contourn else R.drawable.routes_contourn),
                    contentDescription = "Les meves rutes.",
                    tint = if (currentRoute == "rutes") selectedColor else unselectedColor,
                    modifier = Modifier.size(49.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "recompenses",
            onClick = { navController.navigate("recompenses") },
            icon = {
                Icon(
                    painter = painterResource(if (currentRoute == "recompenses") R.drawable.rewards_contourn else R.drawable.rewards_contourn),
                    contentDescription = "recompenses disponibles.",
                    tint = if (currentRoute == "recompenses") selectedColor else unselectedColor,
                    modifier = Modifier.size(42.dp)
                )
            }
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = {
                Icon(
                    painter = painterResource(if (currentRoute == "profile") R.drawable.profile_contourn else R.drawable.profile_contourn),
                    contentDescription = "El meu perfil.",
                    tint = if (currentRoute == "profile") selectedColor else unselectedColor,
                    modifier = Modifier.size(42.dp)
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, user: User?, navController: NavController) {
    Row {
        TopAppBar(
            title = { Text(title, color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Tornar enrere",
                        tint = Color.White
                    )
                }
            },
            actions = {
                val saldo = user?.saldoDisponible ?: 0.00
                Card(
                    modifier = Modifier
                        .padding(top = 8.dp, end = 10.dp)
                        .border(2.dp, Color(40, 40, 40), RoundedCornerShape(8.dp))
                        .shadow(6.dp, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black,
                        contentColor = Color.White,
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.padding(start = 100.dp, end = 12.dp, top = 7.dp, bottom = 7.dp)
                    ) {
                        Text(
                            text = "$saldo",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(Modifier.width(12.dp))
                        Icon(
                            painter = painterResource(R.drawable.coins),
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray)
        )
    }
}



