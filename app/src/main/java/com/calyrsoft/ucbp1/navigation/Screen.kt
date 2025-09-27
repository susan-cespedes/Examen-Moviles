package com.calyrsoft.ucbp1.navigation

sealed class Screen(val route: String) {
   object Profile : Screen("profile")
   object Dollar : Screen("dollar")
   object Github : Screen("github")
   object Movie : Screen("movie")
   object Notification : Screen("notification")
}
