package view.right.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import db.Message
import repository.MessagesRepo
import java.awt.event.MouseEvent
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MessageItem(message: Message, messagesRepo: MessagesRepo) {
    val expanded = remember { mutableStateOf(false) }
    val menuItems = MessageAction.values()
    Column(
        modifier = Modifier
            .background(
                color = when (message.isMy) {
                    true -> Color(43, 82, 120)
                    else -> Color(24, 37, 51)
                },
                shape = RoundedCornerShape(5.dp)
            )
            .padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 5.dp)
            .onPointerEvent(PointerEventType.Press) {
                when (it.awtEventOrNull?.button) {
                    MouseEvent.BUTTON3 -> {
                        expanded.value = true
                    }
                }
            }
    ) {
        Text(text = message.text, color = Color.White)
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            val pattern = "hh:mm:ss"
            val formatter = DateTimeFormatter.ofPattern(pattern)
                .withZone(ZoneId.systemDefault())
            val messageCurrentTime = formatter.format(message.currentTime)
            Text(
                text = messageCurrentTime,
                style = MaterialTheme.typography.overline,
                color = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = {
                expanded.value = false
            }
        ) {
            menuItems.forEach {
                DropdownMenuItem(onClick = {
                    when (it) {
                        MessageAction.EDIT -> {
                            println("Edit: " + message.text)
                        }
                        MessageAction.REMOVE -> {
                            messagesRepo.removeMessage(message)
                        }
                    }

                    expanded.value = false
                }) {
                    Text(text = it.text)
                }
            }
        }
    }
}