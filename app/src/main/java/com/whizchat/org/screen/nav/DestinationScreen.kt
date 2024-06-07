package com.whizchat.org.screen.nav

sealed class DestinationScreen (val route : String){
    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object StatusList : DestinationScreen("statusList")
    object OpenChat : DestinationScreen("openChat/{chatId}"){
        fun createRoute(id : String) = "openChat/$id"
    }
    object OpenStatus : DestinationScreen("openStatus/{userId}"){
        fun createRoute(userId : String) = "openChat/$userId"
    }
    object OpenChatBot : DestinationScreen("chatBot")
}

