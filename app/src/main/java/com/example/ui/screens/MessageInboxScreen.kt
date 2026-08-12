package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CampusViewModel

@Composable
fun MessageInboxScreen(viewModel: CampusViewModel, onChatClick: (Int) -> Unit) {
    val dms by viewModel.directMessages.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDirectMessages()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Messages", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 16.dp))
        
        if (dms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No messages yet. Start a conversation from a profile or gig!", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn {
                items(dms) { dm ->
                    val otherUserEmail = if (dm.participant1Email == currentUser?.email) dm.participant2Email else dm.participant1Email
                    ListItem(
                        headlineContent = { Text(otherUserEmail, fontWeight = FontWeight.SemiBold) },
                        supportingContent = { Text(dm.lastMessage, maxLines = 1) },
                        trailingContent = { Text(java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(dm.lastMessageTimestamp)) },
                        modifier = Modifier.clickable { onChatClick(dm.id) }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
