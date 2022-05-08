package view.dialog

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import model.User
import repository.ConversationsRepo
import repository.UsersRepo
import resources.defaultBackgroundColor
import resources.selectedBackgroundColor
import view.item.user.UserItem

/**
 * @author Markitanov Vadim
 * @since 07.05.2022
 */
@Composable
fun AddMembers(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    val interactionSource = remember { MutableInteractionSource() }
    val selected = mutableStateOf<User?>(null)

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
            val usersLazyListState = rememberLazyListState()
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text(
                    text = "Add Members",
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )

                val name = remember { mutableStateOf(TextFieldValue()) }
                val nameEmpty = remember { mutableStateOf(false) }

                Box(modifier = Modifier.height(300.dp)) {
                    LazyColumn(
                        modifier = Modifier.width(350.dp),
                        state = usersLazyListState
                    ) {
                        items(items = usersRepo.all()) { user ->
                            UserItem(
                                user = user,
                                modifier = Modifier
                                    .background(
                                        if (selected.value == user) selectedBackgroundColor
                                        else defaultBackgroundColor
                                    )
                                    .fillMaxWidth()
                                    .selectable(user == selected.value,
                                        onClick = {
                                            println("CLICK")
                                            if (selected.value != user) {
                                                selected.value = user
                                            }
                                        }
                                    )
                            )
                        }
                    }

                    VerticalScrollbar(
                        modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                        adapter = rememberScrollbarAdapter(
                            scrollState = usersLazyListState
                        )
                    )
                }

                usersRepo.requestAll()

                Row(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
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