package com.whizchat.org.screen.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.whizchat.org.data.navigateTo
import com.whizchat.org.screen.nav.BottomNavigationItem
//import com.google.accompanist.blur.Blur

@Composable
fun BottomNavScreen(
    selectedItem: BottomNavigationItem,
    navController: NavController
) {
    val backgroundColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)



       Row(
           modifier = Modifier
               .fillMaxWidth()
               .wrapContentHeight()
               .padding(4.dp)

               .background(backgroundColor),
//            .blur(radius = 10.dp),
           horizontalArrangement = Arrangement.SpaceEvenly,
           verticalAlignment = Alignment.Bottom
       ) {

               for (item in BottomNavigationItem.entries) {
                   Image(
                       painter = painterResource(id = item.icon),
                       contentDescription = null,
                       modifier = Modifier
                           .size(40.dp)
                           .padding(4.dp)

                           .clickable {
                               navigateTo(navController, item.navDestination.route)
                           },
                       colorFilter = if (item == selectedItem) ColorFilter.tint(color = Color.Magenta)
                       else ColorFilter.tint(color = Color.Gray)
                   )
               }
           }



}

