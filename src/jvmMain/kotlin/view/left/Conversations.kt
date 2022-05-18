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
import repository.ConversationsRepo
import repository.UsersRepo
import view.left.item.ConversationItem

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun Conversations(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    Box(modifier = Modifier.fillMaxSize().background(Color(14, 22, 33))) {
        val usersLazyListState = rememberLazyListState()

        LazyColumn(
            state = usersLazyListState,
        ) {
            items(items = conversationsRepo.all()) { conversation ->
                val modifier = Modifier
                    .background(
                        if (conversationsRepo.selected().value == conversation) Color(43, 82, 120)
                        else Color(23, 33, 43)
                    )
                    .fillMaxWidth()
                    .selectable(conversation == conversationsRepo.selected().value,
                        onClick = {
                            if (conversationsRepo.selected().value != conversation) {
                                conversationsRepo.selected().value = conversation
                            }
                        })

//                if (conversation.companionId == null) {
                    ConversationItem(
                        modifier = modifier,
                        conversation = conversation,
                        conversationsRepo = conversationsRepo,
                        usersRepo = usersRepo
                    )
//                } else {
//                    val companion: User? = usersRepo.getById(conversation.companionId)
//                    CompanionItem(
//                        conversation = conversation,
//                        companion,
//                        modifier = modifier,
//                        repo = conversationsRepo
//                    )
//                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = usersLazyListState
            )
        )
    }
}