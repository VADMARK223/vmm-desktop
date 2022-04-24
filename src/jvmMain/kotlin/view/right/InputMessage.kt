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
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import repository.MessagesRepo

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun InputMessage(messagesRepo: MessagesRepo) {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }
    val mainOutputEmpty = mutableStateOf(mainOutput.value.text.isNotEmpty())
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = mainOutput.value,
            onValueChange = {
                mainOutput.value = it
            },
            modifier = Modifier.fillMaxWidth().onKeyEvent {
                if (it.key == Key.Enter) {
                    sendMessage(mainOutput, messagesRepo)
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
                        onClick = {
                            println("Add emoji")
                        }
                    ) {
                        Icon(Icons.Filled.Face, contentDescription = "Add emoji")
                    }

                    if (mainOutputEmpty.value) {
                        IconButton(
                            onClick = {
                                sendMessage(mainOutput, messagesRepo)
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

private fun sendMessage(mainOutput: MutableState<TextFieldValue>, messagesRepo: MessagesRepo) {
    messagesRepo.addMessage(mainOutput.value.text)
    mainOutput.value = TextFieldValue("")
}
