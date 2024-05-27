package com.whizchat.org.screen.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.screen.auth.*
import com.whizchat.org.screen.chatscreen.ChatListScreen
import com.whizchat.org.screen.chatscreen.OpenChatScreen
import com.whizchat.org.screen.component.ProfileScren
import com.whizchat.org.screen.status.OpenStatusScreen
import com.whizchat.org.screen.status.StatusScreen


@Composable
fun WhizAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewmodel = hiltViewModel<WapViewModel>()
    NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route){
        composable(DestinationScreen.SignUp.route){
            SignUpScreen(navController,viewmodel)
        }
        composable(DestinationScreen.Login.route){
              LoginScreen(navController,viewmodel)
        }
        composable(DestinationScreen.ChatList.route){
              ChatListScreen(navController,viewmodel)
        }
        composable(DestinationScreen.Profile.route){
              ProfileScren(navController,viewmodel)
        }
      composable(DestinationScreen.StatusList.route){
              StatusScreen(navController, viewmodel)
        }
        composable(DestinationScreen.OpenChat.route){
            val chatId = it.arguments?.getString("chatId")
            chatId?.let{
                OpenChatScreen(navController, viewmodel , it)

            }
        }
        composable(DestinationScreen.OpenStatus.route){
            val userId = it.arguments?.getString("userId")
            userId?.let{
            OpenStatusScreen(navController, viewmodel ,  it)
        }

        }

    }
}