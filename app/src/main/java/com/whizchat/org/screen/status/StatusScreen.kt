package com.whizchat.org.screen.status

import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.screen.component.BottomNavScreen
import com.whizchat.org.screen.nav.BottomNavigationItem

@Composable
fun StatusScreen(navController: NavController, vm : WapViewModel) {

    BottomNavScreen(selectedItem = BottomNavigationItem.STATUSLIST , navController = navController)
}