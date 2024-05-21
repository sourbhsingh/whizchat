package com.whizchat.org.screen.auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.whizchat.org.R
import com.whizchat.org.WapViewModel

import com.whizchat.org.data.CommonProgressBar
import com.whizchat.org.data.checkSignedIn
import com.whizchat.org.data.navigateTo
import com.whizchat.org.screen.nav.DestinationScreen

@Composable
fun LoginScreen(navController: NavController, vm : WapViewModel) {

   checkSignedIn(vm , navController)

   Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart ) {
      Column(modifier = Modifier
         .fillMaxSize()
         .wrapContentSize()
         .verticalScroll(
            rememberScrollState()
         ),
         horizontalAlignment = Alignment.CenterHorizontally) {

         val emailState = remember {
            mutableStateOf(TextFieldValue())
         }
         val passState = remember {
            mutableStateOf(TextFieldValue())
         }
         val focus = LocalFocusManager.current
         Image(
            painter = painterResource(id = R.drawable.chat),
            contentDescription = null,
            modifier = Modifier
               .width(200.dp)
               .padding(top = 16.dp)
               .padding(8.dp)
         )
         Text(text ="Sign In", fontSize = 30.sp ,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier
               .padding(8.dp)
               .padding(horizontal = 30.dp),
         )


         OutlinedTextField(
            value = emailState.value,
            onValueChange = {
               emailState.value = it
            },
            label = { Text(text = "Email")},
            modifier = Modifier.padding(8.dp)

         )
         OutlinedTextField(
            value = passState.value,
            onValueChange = {
               passState.value = it
            },
            label = { Text(text = "Password")},
            modifier = Modifier.padding(8.dp)

         )
         Button(onClick = {
            vm.login(

               emailState.value.text,
               passState.value.text
            )

         },
            colors =  ButtonDefaults.buttonColors(Color.Magenta)
            ,modifier =
            Modifier.padding(8.dp)

         ){
            Text(text = "SIGN IN")
         }

         Text(text = "New User? Click here to SignUp ->" , color= Color.Blue ,
            modifier = Modifier
               .padding(8.dp)
               .clickable {
                  navigateTo(navController, DestinationScreen.SignUp.route)
               })
      }


      if (vm.inProgress.value){
         CommonProgressBar()
      }
   }


}