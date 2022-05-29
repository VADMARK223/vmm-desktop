package view.left.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box {
                Avatar(conversation.visibleName.value, mutableStateOf(companion?.online).value, companion)

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (conversation.companionId != null) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = conversation.visibleName.value,
                        style = MaterialTheme.typography.body2,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )

                    Spacer(Modifier.weight(1F))

                    val formatter = DateTimeFormatter
                        .ofPattern("hh:mm:ss")
                    val updateTimeText = formatter.format(conversation.updateTime.toJavaLocalDateTime())

                    Text(
                        maxLines = 1,
                        text = updateTimeText,
                        style = MaterialTheme.typography.body2,
                        color = Color.Gray
                    )
                }

                if (conversation.lastMessageVisible.value != null) {
                    val lastMessageText =
                        if (UsersRepo.current().value?.id != conversation.lastMessageVisible.value?.ownerId)
                            conversation.lastMessageVisible.value?.text ?: ""
                        else "You: ${conversation.lastMessageVisible.value?.text}"
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