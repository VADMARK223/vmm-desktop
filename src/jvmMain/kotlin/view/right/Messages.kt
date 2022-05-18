package view.right

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import repository.ConversationsRepo
import repository.MessagesRepo
import repository.UsersRepo
import view.right.item.MessageItem

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Messages(
    modifier: Modifier,
    mainOutput: MutableState<TextFieldValue>,
    messagesRepo: MessagesRepo,
    usersRepo: UsersRepo,
    conversationsRepo: ConversationsRepo
) {
    val conversation = conversationsRepo.selected()
//    println("MESSAGES REDRAW")

    if (conversation.value != null) {
        val conversationId = conversation.value?.id as Long
        messagesRepo.messagesByConversationId(conversationId)
    }

    val messages = messagesRepo.all()

    Box(modifier = modifier) {
        val lazyListState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp),
            state = lazyListState,
            reverseLayout = true,
        ) {
            itemsIndexed(items = messages/*messagesRepo.all()*/.reversed()) { index, message ->
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = when (message.ownerId == usersRepo.current().value?.id) {
                        true -> Arrangement.End
                        else -> Arrangement.Start
                    }
                ) {
                    item {
                        MessageItem(message, mainOutput, messagesRepo, usersRepo)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }

        val firstItemVisible = remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex == 0
            }
        }

        if (!firstItemVisible.value) {
            Box(Modifier.fillMaxSize().padding(12.dp), Alignment.BottomEnd) {
                FloatingActionButton(onClick = {
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(index = 0)
                    }
                }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = lazyListState
            ),
            reverseLayout = true
        )
    }
}