package view.window

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.ConversationsRepo
import common.User
import common.UsersRepo
import resources.defaultBackgroundColor
import resources.selectedBackgroundColor
import service.printDraw
import view.common.Search
import view.item.user.UserItem
import java.awt.Cursor
import java.util.*

/**
 * @author Markitanov Vadim
 * @since 07.05.2022
 */
@Composable
fun AddMembers(conversationName: String) {
    printDraw()
    val selectedList = remember { mutableStateListOf<User>() }
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    val allUsers = remember { UsersRepo.requestAll(true) }

    val usersLazyListState = rememberLazyListState()
    val createButtonEnabled = remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.width(400.dp)
    ) {
        Text(
            text = "Add Members",
            color = Color.White,
            style = MaterialTheme.typography.h6
        )

        Search(searchState)

        Box(modifier = Modifier.height(300.dp)) {
            var filteredUsers: List<User>
            LazyColumn(
                state = usersLazyListState
            ) {
                val searchedText = searchState.value.text
                filteredUsers = if (searchedText.isEmpty()) {
                    allUsers
                } else {
                    val resultList = ArrayList<User>()
                    val searchedTextLowercase = searchedText.lowercase(Locale.getDefault())
                    for (user in allUsers) {
                        if (user.name.lowercase(Locale.getDefault()).contains(searchedTextLowercase)) {
                            resultList.add(user)
                        }
                    }
                    resultList
                }

                items(items = filteredUsers) { user ->
                    UserItem(
                        user = user,
                        modifier = Modifier
                            .background(
                                if (selectedList.contains(user)) selectedBackgroundColor else defaultBackgroundColor
                            )
                            .pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
                            .fillMaxWidth()
                            .selectable(selected = selectedList.contains(user),
                                onClick = {
                                    if (!selectedList.contains(user)) {
                                        selectedList.add(user)
                                    } else {
                                        selectedList.remove(user)
                                    }

                                    createButtonEnabled.value = selectedList.isNotEmpty()
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

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                modifier = Modifier.pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                onClick = {
                    Window.show(WindowType.NEW_CONVERSATION)
                },
            ) {
                Text("Cancel")
            }
            if (createButtonEnabled.value) {
                Button(
                    onClick = {
                        if (selectedList.isNotEmpty()) {
                            ConversationsRepo.put(
                                conversationName,
                                UsersRepo.current().value?.id,
                                selectedList.toList(),
                                null
                            )
                            Window.hide()
                        }
                    },
//                        enabled = createButtonEnabled.value
                ) {
                    Text("Create")
                }
            }
        }
    }
}