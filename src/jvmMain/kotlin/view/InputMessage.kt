package view

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import data.Message
import repository.MessagesRepo

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun InputMessage(messagesRepo: MessagesRepo) {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }
    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = mainOutput.value,
            onValueChange = {
                mainOutput.value = it
            },
            modifier = Modifier.fillMaxWidth(),
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

                    IconButton(
                        onClick = {
                            messagesRepo.addMessage(Message(21, mainOutput.value.text))
                            mainOutput.value = TextFieldValue("")
                        }
                    ) {
                        Icon(Icons.Filled.Send, contentDescription = "Send message")
                    }
                }
            }
        )
    }
}