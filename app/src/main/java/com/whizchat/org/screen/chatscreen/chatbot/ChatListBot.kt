package com.whizchat.org.screen.chatscreen.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.whizchat.org.data.ChatBotData
import com.whizchat.org.data.ChatBotRoleEnum
import com.whizchat.org.data.Message

@Composable
fun ChatListBot(
    list: MutableList<ChatBotData>
) {

    LazyColumn (Modifier.fillMaxSize()){
        items(list){
                msg ->
            val alignment = if(msg.role == ChatBotRoleEnum.USER.role) Alignment.End else Alignment.Start
            val color = if(msg.role==ChatBotRoleEnum.MODEL.role) Color(0xFF68C400) else Color(0xFFC0B1C0)
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalAlignment = alignment
            ) {
                Text(text = msg.message?:"" , modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(color)
                    .padding(12.dp),
                    fontWeight = FontWeight.Bold)
            }
        }
    }
}

