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
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import db.Conversation
import kotlinx.datetime.toJavaLocalDateTime
import model.User
import repository.ConversationsRepo
import repository.UsersRepo
import view.common.Avatar
import java.awt.event.MouseEvent
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConversationItem(
    conversation: Conversation,
    conversationsRepo: ConversationsRepo,
    modifier: Modifier,
    usersRepo: UsersRepo
) {
    val expanded = remember { mutableStateOf(false) }
    val menuItems = ConversationAction.values()
    val companion: User? = usersRepo.getById(conversation.companionId)

    Box(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp).onPointerEvent(PointerEventType.Press) {
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
                Avatar(avaText, true)

                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = {
                        expanded.value = false
                    }
                ) {
                    menuItems.forEach {
                        DropdownMenuItem(onClick = {
                            when (it) {
                                ConversationAction.DELETE -> {
                                    conversationsRepo.delete(conversation)
                                }
                            }

                            expanded.value = false
                        }) {
                            Text(text = it.text)
                        }
                    }
                }
            }

            Row {
                val itemText =
                    if (companion != null) companion.firstName + " " + companion.lastName else conversation.name
                Text(
                    text = itemText,
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
            }

            Spacer(Modifier.weight(1F))

            val formatter = DateTimeFormatter
                .ofPattern("hh:mm:ss")
            val updateTimeText = formatter.format(conversation.updateTime.toJavaLocalDateTime())

            Text(
                text = updateTimeText,
                color = Color.White
            )
        }
    }
}