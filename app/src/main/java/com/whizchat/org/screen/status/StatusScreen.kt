package com.whizchat.org.screen.status

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.screen.component.BottomNavScreen
import com.whizchat.org.screen.nav.BottomNavigationItem

@Composable
fun StatusScreen(navController: NavController, vm : WapViewModel) {
    Scaffold {

        Column(modifier = Modifier.fillMaxSize()
            .padding(it),
            ) {
            Column (modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                , horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center ,
                content = { androidx.compose.material3.Text(text = "No Status Available") })


            BottomNavScreen(
                selectedItem = BottomNavigationItem.STATUSLIST,
                navController = navController
            )

        }
    }

}