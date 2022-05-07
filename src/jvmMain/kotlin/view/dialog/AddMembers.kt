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

/**
 * @author Markitanov Vadim
 * @since 07.05.2022
 */
@Composable
fun AddMembers(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5F))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                Dialog.state.value = DialogState.HIDE
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
                val name = remember { mutableStateOf(TextFieldValue()) }
                val nameEmpty = remember { mutableStateOf(false) }

                Text("asdasd")

                /*TextField(
                    value = name.value,
                    onValueChange = {
                        name.value = it
                    },
                    label = {
                        Text("Add members")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color(23, 33, 43),
                        textColor = Color.White
                    ),
                    isError = nameEmpty.value
                )*/

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
//                            Dialog.state.value = DialogState.HIDE
                            Dialog.state.value = DialogState.NEW_CONVERSATION_WITH_MEMBERS
                        },
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            if (name.value.text.isEmpty()) {
                                nameEmpty.value = true
                            } else {
                                conversationsRepo.create(name.value.text, usersRepo.current().value?.id)
                                Dialog.state.value = DialogState.HIDE
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