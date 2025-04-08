package com.example.km.navigation

import android.content.pm.verify.domain.DomainVerificationUserState
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.R
import com.example.km.RutaManagment.ui.screens.RutesScreen
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.screens.LoginScreen
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.User
import com.example.km.main.screens.MainScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(puntGPSViewModel: PuntGPSViewModel, rutaViewModel: RutaViewModel, navController: NavController, loginViewModel: LoginViewModel, userState: State<User?>) {
    val navController = rememberNavController()
   // val rutaViewModel: RutaViewModel = viewModel()
    //val puntGPSViewModel: PuntGPSViewModel = viewModel()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel, userState ) }


        // Pantalla Home amb Bottom Navigation
        composable("home") { MainScreen(rutaViewModel, puntGPSViewModel, navController, userState) }

        composable("rutes") { RutesScreen(rutaViewModel, puntGPSViewModel, navController, userState) }
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

