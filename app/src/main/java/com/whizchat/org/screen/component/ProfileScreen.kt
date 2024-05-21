package com.whizchat.org.screen.component

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.whizchat.org.WapViewModel
import com.whizchat.org.data.CommonDivider
import com.whizchat.org.data.CommonImage
import com.whizchat.org.data.CommonProgressBar
import com.whizchat.org.data.navigateTo
import com.whizchat.org.screen.nav.BottomNavigationItem
import com.whizchat.org.screen.nav.DestinationScreen

@Composable
fun ProfileScren( navController: NavController ,vm : WapViewModel ) {
    val inProcess = vm.inProgress.value
    if (inProcess){
        CommonProgressBar()
    }
    else{
        val userData =vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name?:"")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number?:"")
        }
        Column () {


            ProfileContent(modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(8.dp),
                vm = vm,
                name = name,
                number = number,
                onNameChange = { name=it },
                onNumberChange = { number=it },
                onSave = {
                    vm.createOrUpdateProfile(name = name , number = number)
                },
                onBack = {
                    navigateTo(navController =navController, route =  DestinationScreen.ChatList.route)
                },
                onLogOut = {

                    vm.logout()
                    navigateTo(navController = navController , route =  DestinationScreen.Login.route)
                }

            )
            Spacer(modifier = Modifier.weight(1f))
            BottomNavScreen(
                selectedItem = BottomNavigationItem.PROFILE,
                navController = navController
            )
        }
        }


}

@Composable
fun ProfileContent(
    modifier: Modifier,
    vm: WapViewModel,
    name: String,
    number: String,
    onNameChange : (String)-> Unit,
    onNumberChange : (String)-> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogOut : ()-> Unit
) {
    val imageUrl = vm.userData.value?.imageUrl
    Column {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .statusBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Back", modifier = Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save",  modifier = Modifier.clickable{
                onSave.invoke()
            })


        }
        CommonDivider()
        ProfileImage(imageUrl = imageUrl , vm=vm )
        CommonDivider()
        Row (
            modifier= Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(value = name, onValueChange = onNameChange)

        }
        Row (
            modifier= Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(value = number, onValueChange = onNumberChange)

        }
        CommonDivider()
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.Center) {
            Text(text = "LogOut", modifier = Modifier.clickable {
                onLogOut.invoke()
            })
        }
    }

}


@Composable
fun ProfileImage(imageUrl : String? , vm : WapViewModel) {
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)){
        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                launcher.launch("image/*")
            },
            horizontalAlignment = Alignment.CenterHorizontally) {
            Card(shape = CircleShape, modifier = Modifier
                .padding(8.dp)
                .size(100.dp)) {
                CommonImage(data = imageUrl)
            }
            Text(text ="Change Profile Picture")
        }
        if (vm.inProgress.value){
            CommonProgressBar()
        }
    }
}