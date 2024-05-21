package com.whizchat.org.screen.chatscreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.screen.component.BottomNavScreen
import com.whizchat.org.screen.nav.BottomNavigationItem

@Composable
fun ChatListScreen( navController: NavController, vm : WapViewModel) {

    Column(

        modifier = Modifier.fillMaxSize()
    ) {
        // Content of ProfileScreen
        // Add your profile content here

            Text(text = "Hello you are in text screen ${vm.userData.value?.name}")



        // Spacer to occupy remaining space
        Spacer(modifier = Modifier.weight(1f))

        // BottomNavScreen aligned to the bottom
        BottomNavScreen(selectedItem = BottomNavigationItem.CHATLIST , navController = navController)
    }


}

