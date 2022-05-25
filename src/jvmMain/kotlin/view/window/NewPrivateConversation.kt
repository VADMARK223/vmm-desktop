package view.window

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import common.ConversationsRepo
import common.User
import common.UsersRepo
import view.common.Search
import view.item.user.UserItem
import java.awt.Cursor
import java.util.*

/**
 * @author Markitanov Vadim
 * @since 10.05.2022
 */
@Composable
fun NewPrivateConversation() {
    val searchState = remember { mutableStateOf(TextFieldValue("")) }
    val usersLazyListState = rememberLazyListState()
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.width(400.dp)
    ) {
        Search(searchState)

        Box(modifier = Modifier.height(300.dp)) {
            val users = UsersRepo.requestAll(true)
            var filteredUsers: List<User>
            LazyColumn(
                state = usersLazyListState
            ) {
                val searchedText = searchState.value.text
                filteredUsers = if (searchedText.isEmpty()) {
                    users
                } else {
                    val resultList = ArrayList<User>()
                    val searchedTextLowercase = searchedText.lowercase(Locale.getDefault())
                    for (user in users) {
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
                            .fillMaxWidth()
                            .pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
                            .clickable {
                                ConversationsRepo.put(
                                    user.name,
                                    UsersRepo.current().value?.id,
                                    listOf(user),
                                    user.id
                                )
                                Window.hide()
                            }
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
                    Window.hide()
                },
            ) {
                Text("Cancel")
            }
        }
    }
}