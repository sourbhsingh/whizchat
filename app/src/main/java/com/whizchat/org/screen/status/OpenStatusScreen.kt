package com.whizchat.org.screen.status

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.screen.nav.BottomNavigationItem

@Composable
fun OpenStatusScreen(navController: NavController
, vm: WapViewModel , chatId : String ) {

    Text(text = chatId)
}
