package view.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import repository.ConversationsRepo
import repository.UsersRepo
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 07.05.2022
 */
@Composable
fun NewConversationWithMembers() {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5F))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                Dialog.hide()
            }
    ) {
        Box(
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {}
                .align(Alignment.Center)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(23, 33, 43))
                .padding(25.dp)

        ) {
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
                            Dialog.hide()
                        },
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (name.value.text.isEmpty()) {
                                nameEmpty.value = true
                            } else {
                                Dialog.state.value = DialogState(DialogType.ADD_MEMBERS, name.value.text)
                            }
                        },
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}