package com.calyrsoft.ucbp1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.calyrsoft.ucbp1.features.dollar.presentation.DollarScreen
import com.calyrsoft.ucbp1.features.movies.presentation.screen.MoviesScreen
import com.calyrsoft.ucbp1.features.notification.presentation.NotificationScreen
import com.calyrsoft.ucbp1.features.profile.presentation.ProfileScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Profile.route // 🚀 Inicio directo en GitHub
    ) {
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Dollar.route) {
            DollarScreen(navController = navController)
        }
        composable(Screen.Movie.route) {
            MoviesScreen(navController = navController)
        }
        composable(Screen.Notification.route) {
            NotificationScreen(navController = navController)
        }
    }
}
