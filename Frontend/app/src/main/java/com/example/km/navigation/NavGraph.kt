package com.example.km.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.km.UserManagment.ui.screens.LoginScreen
import com.example.km.UserManagment.ui.screens.RegisterScreen
import com.example.km.main.screens.MainScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController,) }
        composable("register") { RegisterScreen(navController) }


        // Pantalla Home amb Bottom Navigation
        composable("home") { MainScreen(navController) }
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
