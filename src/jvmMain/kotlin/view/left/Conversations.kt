package view.left

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import db.User
import service.HttpService

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun Conversations(modifier: Modifier, onUserClick: (User) -> Unit) {
    Box(modifier = modifier) {
        val usersLazyListState = rememberLazyListState()

        LazyColumn(
            state = usersLazyListState,
            modifier = modifier
        ) {
            items(items = HttpService.getConversationList()) { conversation ->
                Text(text = conversation.name, color = Color.Red)
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = usersLazyListState
            )
        )

        HttpService.requestAllConversation(rememberCoroutineScope())
    }
}