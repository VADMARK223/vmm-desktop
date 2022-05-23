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
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import model.Conversation
import repository.ConversationsRepo
import view.left.item.ConversationItem
import java.util.*

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Composable
fun Conversations(
    conversationsRepo: ConversationsRepo,
    searchState: MutableState<TextFieldValue>
) {
    Box(modifier = Modifier.fillMaxSize().background(Color(14, 22, 33))) {
        val usersLazyListState = rememberLazyListState()
        var filteredConversations: List<Conversation>
        LazyColumn(
            state = usersLazyListState,
        ) {
            val searchedText = searchState.value.text
            filteredConversations = if (searchedText.isEmpty()) {
                conversationsRepo.all()
            } else {
                val resultList = ArrayList<Conversation>()

                val searchedTextLowercase = searchedText.lowercase(Locale.getDefault())
                for (conversation in conversationsRepo.all()) {
                    if (conversation.name.lowercase().contains(searchedTextLowercase)) {
                        resultList.add(conversation)
                    }
                }

                resultList
            }

            items(items = filteredConversations) { conversation ->
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
                    conversationsRepo = conversationsRepo
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