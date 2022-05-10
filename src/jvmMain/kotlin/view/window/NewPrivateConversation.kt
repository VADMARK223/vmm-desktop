package view.window

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
import view.common.Search
import view.item.user.UserItem
import java.util.*

/**
 * @author Markitanov Vadim
 * @since 10.05.2022
 */
@Composable
fun NewPrivateConversation(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    val interactionSource = remember { MutableInteractionSource() }
    val selected = mutableStateOf<User?>(null)
    val searchState = remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(0.5F))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                Window.hide()
            }
    ) {
        Box(
            modifier = Modifier
                .width(400.dp)
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
                    text = "Add Member",
                    color = Color.White,
                    style = MaterialTheme.typography.h6
                )

                Search(searchState)

                Box(modifier = Modifier.height(300.dp)) {
                    val users = usersRepo.all()
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
                                if (user.firstName.lowercase(Locale.getDefault()).contains(searchedTextLowercase) ||
                                    user.lastName.lowercase(Locale.getDefault()).contains(searchedTextLowercase)
                                ) {
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
                                        if (selected.value == user) selectedBackgroundColor
                                        else defaultBackgroundColor
                                    )
                                    .fillMaxWidth()
                                    .selectable(user == selected.value,
                                        onClick = {
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
                            if (selected.value != null) {
                                conversationsRepo.create(
                                    selected.value?.firstName + " " + selected.value?.lastName,
                                    usersRepo.current().value?.id,
                                    listOf(selected.value as User),
                                    true
                                )
                                Window.hide()
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