package view.window

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 07.05.2022
 */
@Composable
fun NewConversation() {
    Column {
        val name = remember { mutableStateOf(TextFieldValue("Conversation #" + Random.nextInt(1000))) }
        val nameEmpty = remember { mutableStateOf(false) }

        TextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            label = {
                Text("Group name")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(23, 33, 43),
                textColor = Color.White
            ),
            isError = nameEmpty.value
        )

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    Window.hide()
                },
            ) {
                Text("Cancel")
            }
            Button(
                onClick = {
                    if (name.value.text.isEmpty()) {
                        nameEmpty.value = true
                    } else {
                        Window.state.value = WindowState(WindowType.ADD_MEMBERS, name.value.text)
                    }
                },
            ) {
                Text("Next")
            }
        }
    }
}