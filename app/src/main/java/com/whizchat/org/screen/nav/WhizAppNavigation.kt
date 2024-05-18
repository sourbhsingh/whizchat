package com.whizchat.org.screen.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.screen.DestinationScreen
import com.whizchat.org.screen.auth.*


@Composable
fun WhizAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewmodel = hiltViewModel<WapViewModel>()
    NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
        composable(DestinationScreen.SignUp.route){
            SignUpScreen(navController,viewmodel)
        }
        composable(DestinationScreen.Login.route){
//            LoginScreen(navController,viewmodel)
        }
    }
}