package com.whizchat.org.screen

import androidx.navigation.NavController

fun navigateTo(navController: NavController, route : String){
    navController.navigate(route){
        popUpTo(route)
        launchSingleTop = true
    }
}