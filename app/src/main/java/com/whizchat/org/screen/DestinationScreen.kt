package com.whizchat.org.screen

sealed class DestinationScreen (val route : String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object OpenChat : DestinationScreen("openChat/{chatId}"){
        fun createRoute(id : String) = "openChat/$id"
    }
    object OpenStatus : DestinationScreen("openStatus/{userId}"){
        fun createRoute(userId : String) = "openChat/$userId"
    }
}