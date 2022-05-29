package view.right

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.ConversationsRepo
import common.Message
import common.MessagesRepo
import common.UsersRepo
import service.ImageChooser
import service.printDraw
import java.awt.Cursor

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputMessage() {
    printDraw()
    val showImageChooser = remember { mutableStateOf(false) }

    val mainOutputEmpty = mutableStateOf(InputMessageState.textOutput.value.text.isNotEmpty())
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = InputMessageState.textOutput.value,
            onValueChange = {
                InputMessageState.textOutput.value = it
            },
            modifier = Modifier.fillMaxWidth().onKeyEvent {
                if (it.key == Key.Enter || it.key == Key.NumPadEnter) {
                    putMessage()
                }
                false
            },
            placeholder = { Text("Write a message...") },
            maxLines = 1,
            shape = RoundedCornerShape(0.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            ),
            leadingIcon = {
                IconButton(
                    modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                    onClick = {
                        showImageChooser.value = true
                    }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add file")
                }
            },
            trailingIcon = {
                Row {
                    IconButton(
                        modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                        onClick = {
                            println("Add emoji")
                        }
                    ) {
                        Icon(Icons.Filled.Face, contentDescription = "Add emoji")
                    }

                    if (mainOutputEmpty.value) {
                        IconButton(
                            modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                            onClick = {
                                putMessage()
                            }
                        ) {
                            Icon(
                                if (InputMessageState.editMessage.value != null) Icons.Filled.Done else Icons.Filled.Send,
                                contentDescription = "Put message",
                                tint = Color(82, 136, 193)
                            )
                        }
                    }
                }
            }
        )
    }

    if (showImageChooser.value) {
        ImageChooser {
            println("AAAAAAAAAAAAAAAAAAAAAA $it")
        }
    }
}

private fun putMessage() {
    val message = InputMessageState.editMessage.value
    if (InputMessageState.textOutput.value.text.isNotEmpty()) {
        val conversationSelectedId = ConversationsRepo.selected().value?.id
        val currentUserId = UsersRepo.current().value?.id

        if (message == null) {
            val newMessage = Message(
                text = InputMessageState.textOutput.value.text,
                conversationId = conversationSelectedId,
                ownerId = currentUserId
            )

            println("PUT NEW>")
            MessagesRepo.put(newMessage)
        } else {
            println("EDIT")
            MessagesRepo.put(
                message.copy(
                    text = InputMessageState.textOutput.value.text,
                    edited = true
                )
            )
        }


//        MessagesRepo.put(InputMessageState.textOutput.value.text, conversationSelectedId, currentUserId, message)
        InputMessageState.textOutput.value = TextFieldValue("")

        if (InputMessageState.editMessage.value != null) {
            InputMessageState.editMessage.value = null
        }
    }
}
