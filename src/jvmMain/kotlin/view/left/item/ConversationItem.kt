package view.left.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import db.Conversation
import kotlinx.datetime.toJavaLocalDateTime
import repository.ConversationsRepo
import java.awt.event.MouseEvent
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConversationItem(conversation: Conversation, repo: ConversationsRepo, modifier: Modifier) {
    val bgColor = remember { mutableStateOf(Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))) }
    val expanded = remember { mutableStateOf(false) }
    val menuItems = ConversationAction.values()

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
            val size = 30.dp
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(size))
            ) {
//                val avaText = user.firstName.first().toString() + user.lastName.first().toString()
                val avaText = conversation.name.first().toString()
                Text(
                    text = avaText,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = bgColor.value
                        )
                        .padding(size / 2)
                )

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
                                    repo.remove(conversation)
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
                val itemText = "${conversation.id} ${conversation.name}"
                Text(
                    text = itemText,
                    style = MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )

                if (conversation.isPrivate) {
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