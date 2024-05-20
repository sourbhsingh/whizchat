package com.whizchat.org.screen.chatscreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel

@Composable
fun ChatListScreen( navController: NavController, vm : WapViewModel) {
    Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Hello you are in text screen ${vm.userData.value?.name}")

    }
}