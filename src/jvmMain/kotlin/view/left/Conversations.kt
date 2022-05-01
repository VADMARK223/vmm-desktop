package view.left

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import db.Conversation
import repository.ConversationsRepo

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun Conversations(onConversationClick: (Conversation) -> Unit, repo: ConversationsRepo) {
    Box(modifier = Modifier.fillMaxSize().background(Color(14, 22, 33))) {
        val usersLazyListState = rememberLazyListState()

        LazyColumn(
            state = usersLazyListState,
        ) {
            items(items = repo.all()) { conversation ->
                ConversationItem(
                    conversation = conversation,
                    modifier = Modifier
                        .background(
                            if (repo.selected().value == conversation) Color(
                                43,
                                82,
                                120
                            ) else Color(23, 33, 43)
                        )
                        .fillMaxWidth()
                        .selectable(conversation == repo.selected().value,
                            onClick = {
                                if (repo.selected().value != conversation) {
                                    repo.selected().value = conversation
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