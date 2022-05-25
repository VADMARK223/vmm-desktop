package view.right

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import common.MessagesRepo
import common.UsersRepo
import service.printDraw
import java.awt.Cursor

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputMessage(mainOutput: MutableState<TextFieldValue>) {
    printDraw()
    val mainOutputEmpty = mutableStateOf(mainOutput.value.text.isNotEmpty())
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = mainOutput.value,
            onValueChange = {
                mainOutput.value = it
            },
            modifier = Modifier.fillMaxWidth().onKeyEvent {
                if (it.key == Key.Enter || it.key == Key.NumPadEnter) {
                    sendMessage(mainOutput)
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
                        println("Add file")
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
                                sendMessage(mainOutput)
                            }
                        ) {
                            Icon(Icons.Filled.Send, contentDescription = "Send message", tint = Color(82, 136, 193))
                        }
                    }
                }
            }
        )
    }
}

private fun sendMessage(mainOutput: MutableState<TextFieldValue>) {
    if (mainOutput.value.text.isNotEmpty()) {
        val conversationSelectedId = ConversationsRepo.selected().value?.id
        val currentUserId = UsersRepo.current().value?.id
        MessagesRepo.put(mainOutput.value.text, conversationSelectedId, currentUserId)
        mainOutput.value = TextFieldValue("")
    }
}
