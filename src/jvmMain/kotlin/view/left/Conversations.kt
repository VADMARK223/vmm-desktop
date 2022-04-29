package view.left

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import db.Conversation
import service.HttpService

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun Conversations(modifier: Modifier, onConversationClick: (Conversation) -> Unit) {
    Box(modifier = modifier) {
        val usersLazyListState = rememberLazyListState()

        LazyColumn(
            state = usersLazyListState,
            modifier = modifier
        ) {
            items(items = HttpService.getConversationList()) { conversation ->
//                Text(text = conversation.name, color = Color.Red)
                ConversationItem(
                    conversation = conversation,
                    modifier = Modifier
                        .background(
                            if (HttpService.selectedConversation.value == conversation) Color(
                                43,
                                82,
                                120
                            ) else Color(23, 33, 43)
                        )
                        .fillMaxWidth()
                        .selectable(conversation == HttpService.selectedConversation.value,
                            onClick = {
                                if (HttpService.selectedConversation.value != conversation) {
                                    HttpService.selectedConversation.value = conversation
                                    onConversationClick(conversation)
                                }
                            })
                )
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = usersLazyListState
            )
        )

//        HttpService.requestAllConversation(rememberCoroutineScope())
    }
}