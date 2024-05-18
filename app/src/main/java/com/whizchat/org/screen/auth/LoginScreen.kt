package com.whizchat.org.screen.auth

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel

@Composable
fun LoginScreen(navController: NavController, vm : WapViewModel ) {
   Text(text = " Hii this is login Screen", color = Color.Blue)
}