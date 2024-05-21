package com.whizchat.org.screen.nav

import com.whizchat.org.R

enum class BottomNavigationItem ( val icon : Int  ,val navDestination : DestinationScreen){
    CHATLIST(R.drawable.cicon, DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status, DestinationScreen.StatusList),
    PROFILE(R.drawable.edit, DestinationScreen.Profile)
}