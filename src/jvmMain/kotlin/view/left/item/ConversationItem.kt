package view.left.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import common.Conversation
import common.ConversationsRepo
import common.User
import common.UsersRepo
import kotlinx.datetime.toJavaLocalDateTime
import service.printDraw
import view.common.Avatar
import java.awt.Cursor
import java.awt.event.MouseEvent
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConversationItem(
    modifier: Modifier,
    conversation: Conversation
) {
    printDraw()
    val expanded = remember { mutableStateOf(false) }
    val companion: User? = UsersRepo.getById(conversation.companionId)
    val (forConversation, forChat) = ConversationAction.values().partition { it.isConversation }
    val menuItems = if (companion != null) forChat else forConversation

    Box(
        modifier = modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .onPointerEvent(PointerEventType.Press) {
                    when (it.awtEventOrNull?.button) {
                        MouseEvent.BUTTON3 -> {
                            expanded.value = true
                        }
                    }
                },
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                val avaText =
                    if (companion != null) "${companion.firstName.first()}${companion.lastName.first()}" else conversation.name.first()
                        .toString()
                Avatar(avaText, mutableStateOf(companion?.online).value)

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = {
                        expanded.value = false
                    }
                ) {
                    menuItems.forEach {
                        DropdownMenuItem(onClick = {
                            when (it) {
                                ConversationAction.LEAVE_CHAT -> {
                                    ConversationsRepo.delete(conversation)
                                }

                                ConversationAction.LEAVE_GROUP -> {
                                    ConversationsRepo.delete(conversation)
                                }

                                ConversationAction.CLEAR_HISTORY -> {
                                    println("Clear history.")
                                }
                            }

                            expanded.value = false
                        }) {
                            Text(text = it.text)
                        }
                    }
                }
            }

            Column {
                Row {
                    Text(
                        text = conversation.name,
                        style = MaterialTheme.typography.h6,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )

                    if (conversation.companionId != null) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.weight(1F))

                    val formatter = DateTimeFormatter
                        .ofPattern("hh:mm:ss")
                    val updateTimeText = formatter.format(conversation.updateTime.toJavaLocalDateTime())

                    Text(
                        text = updateTimeText,
                        style = MaterialTheme.typography.overline,
                        color = Color.Gray
                    )
                }

                if (conversation.lastMessage != null) {
                    val lastMessageText =
                        if (UsersRepo.current().value?.id != conversation.lastMessage?.ownerId)
                            conversation.lastMessage?.text ?: ""
                        else "You: ${conversation.lastMessage?.text}"
                    Text(
                        text = lastMessageText,
                        style = MaterialTheme.typography.overline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
            }

        }
    }
}