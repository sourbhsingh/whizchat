package com.whizchat.org.screen.status

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.data.CommonDivider
import com.whizchat.org.data.CommonProgressBar
import com.whizchat.org.data.CommonRow
import com.whizchat.org.data.TitleText
import com.whizchat.org.data.navigateTo
import com.whizchat.org.screen.component.BottomNavScreen
import com.whizchat.org.screen.nav.BottomNavigationItem
import com.whizchat.org.screen.nav.DestinationScreen

@Composable
fun StatusScreen(navController: NavController, vm: WapViewModel) {
    val inProcess = vm.inProgressStatus.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        val status = vm.status.value
        val userData = vm.userData.value
        val myStatus = status.filter {
            it.user.userId == userData?.userId
        }
        val otherStatus = status.filter {
            it.user.userId != userData?.userId
        }
        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri ->
            uri?.let {
                vm.uploadStatus(uri)
            }

        }
        Scaffold(
            floatingActionButton = {
                FAB {
                     launcher.launch(input = "image/*")
                }
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                ) {
                    TitleText(text = "Status")
                    if (status.isEmpty()) {
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            content = { androidx.compose.material3.Text(text = "No Status Available") })
                    } else {

                        if (myStatus.isNotEmpty()) {
                            CommonRow(
                                imageUrl = myStatus[0].user.imageUrl,
                                name = myStatus[0].user.name
                            ) {
                                navigateTo(
                                    navController = navController,
                                    route = DestinationScreen.OpenStatus.createRoute(myStatus[0].user.userId!!)
                                )
                            }

                            CommonDivider()
                            val uniqueUser = otherStatus.map { it.user }.toSet().toList()
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(uniqueUser) { user ->
                                    CommonRow(imageUrl = user.imageUrl, name = user.name) {
                                        navigateTo(
                                            navController = navController,
                                            route = DestinationScreen.OpenStatus.createRoute(user.userId!!)
                                        )
                                    }
                                }
                            }
                        }

                    }
                    BottomNavScreen(
                        selectedItem = BottomNavigationItem.STATUSLIST,
                        navController = navController
                    )




                }
            }
        )
    }


}

@Composable
fun FAB(
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = "Add Status",
            tint = Color.White
        )
    }
}