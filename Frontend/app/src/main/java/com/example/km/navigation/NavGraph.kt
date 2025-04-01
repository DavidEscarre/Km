package com.example.km.navigation

import android.content.pm.verify.domain.DomainVerificationUserState
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.km.PuntGPSManagment.ui.viewmodels.PuntGPSViewModel
import com.example.km.RutaManagment.ui.viewmodels.RutaViewModel
import com.example.km.UserManagment.ui.screens.LoginScreen
import com.example.km.UserManagment.ui.viewmodels.LoginViewModel
import com.example.km.core.models.User
import com.example.km.main.screens.MainScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(navController: NavController, loginViewModel: LoginViewModel, userState: State<User?>) {
    val navController = rememberNavController()
    val rutaViewModel: RutaViewModel = viewModel()
    val puntGPSViewModel: PuntGPSViewModel = viewModel()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel, userState ) }


        // Pantalla Home amb Bottom Navigation
        composable("home") { MainScreen(rutaViewModel, navController, userState) }
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
