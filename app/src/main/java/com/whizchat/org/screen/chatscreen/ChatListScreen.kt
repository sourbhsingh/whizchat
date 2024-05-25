package com.whizchat.org.screen.chatscreen


import android.app.Dialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.data.CommonProgressBar
import com.whizchat.org.data.CommonRow
import com.whizchat.org.data.TitleText
import com.whizchat.org.data.navigateTo
import com.whizchat.org.screen.component.BottomNavScreen
import com.whizchat.org.screen.nav.BottomNavigationItem
import com.whizchat.org.screen.nav.DestinationScreen

@Composable
fun ChatListScreen(navController: NavController, viewModel: WapViewModel) {

    val inProgress = viewModel.inProgressChats
    if (inProgress.value) {
        CommonProgressBar()
    } else {
        val chats = viewModel.chats.value
        val userData = viewModel.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val oFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            viewModel.onAddChat(it)
            showDialog.value = false
        }
        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = oFabClick,
                    onDismiss = onDismiss,
                    onAddChat = onAddChat
                )
            }, content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    TitleText(text = "Chats")
                    if (chats.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "No Chat Available")
                        }
                    }else{
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(chats){chat->
                                val chatUser = if (chat.user1.userId == userData?.userId){
                                    chat.user2
                                }else{
                                    chat.user1
                                }
                                CommonRow(
                                    imageUrl = chatUser.imageUrl,
                                    name = chatUser.name
                                ) {
                                    chat.chatId?.let {
                                        navigateTo(navController, route = DestinationScreen.OpenChat.createRoute(id =it))
                                    }
                                }
                            }
                        }
                    }
                    BottomNavScreen(BottomNavigationItem.CHATLIST, navController)
                }
            }
        )
    }

}
//
//fun ChatListScreen( navController: NavController, vm : WapViewModel) {
//
//    val inProgress = vm.inProgress
//    if(inProgress.value) {
//        CommonProgressBar()
//    }
//    else {
//      val chats =   vm.chats.value
//        val userData = vm.userData.value
//        val showDailogue = remember {
//            mutableStateOf(false)
//        }
//        val onFabClick: () -> Unit= {showDailogue.value= true}
//        val onDismiss:()-> Unit = {
//            showDailogue.value = false
//        }
//       val  onAddChat:(String)-> Unit= {
//           vm.onAddChat(it)
//           showDailogue.value = false
//       }
//
//        Scaffold(floatingActionButton = {
//                FAB(
//                    showDialog = showDailogue.value,
//                    onFabClick = onFabClick,
//                    onDismiss = onDismiss,
//                    onAddChat = onAddChat
//                )
//
//        }
//        , content = {
//            Column(modifier = Modifier
//                .fillMaxSize()
//                .padding(it)) {
//                TitleText(text ="Chats")
//                if(chats.isEmpty()){
//                    Column (modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                    , horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center ,
//                        content = { androidx.compose.material3.Text(text = "No Chats Avialable") })
//
//                }
//                else{
//                    LazyColumn (modifier = Modifier.weight(1f)){
//                        items(chats){
//                            chat ->
//                            val chatUser = if(chat.user1.UserId == userData?.userId){
//                                chat.user2
//                            }
//                            else {
//                                chat.user1
//                            }
//                            CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name) {
//                                chat.chatId?.let {
//                                    navigateTo(navController = navController, route =  DestinationScreen.OpenChat.createRoute(id = it))
//                                }
//
//                            }
//                        }
//                    }
//                }
//
//
//                BottomNavScreen(selectedItem = BottomNavigationItem.CHATLIST , navController = navController)
//
//
//            }
//            }
//            )
//    }
//
//
//
//}


@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss.invoke()
                addChatNumber.value = ""
            },
            confirmButton = {
                Button(onClick = {
                    onAddChat(addChatNumber.value)
                    addChatNumber.value = " "
                }) {                    Text(text = "Add Chat")
                }
            },
            title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
    }
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = Color.Green,
        modifier = Modifier.padding(40.dp)
    ) {
        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
    }
}
