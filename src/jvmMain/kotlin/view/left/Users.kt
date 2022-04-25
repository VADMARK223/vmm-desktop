package view.left

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import db.User
import org.jetbrains.exposed.sql.transactions.transaction
import repository.UsersRepo
import view.common.ContactState
import view.item.user.UserItem

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Users(modifier: Modifier, onUserClick: (User) -> Unit, contactState: MutableState<ContactState>) {
    Box(modifier = modifier) {
        val usersLazyListState = rememberLazyListState()
        LazyColumn(
            state = usersLazyListState,
            modifier = modifier
        ) {
            transaction {
                items(items = UsersRepo.users()) { user ->
                    UserItem(
                        user = user,
                        modifier = Modifier
                            .background(if (UsersRepo.selected.value == user) Color(43, 82, 120) else Color(23, 33, 43))
                            .fillMaxWidth()
                            .selectable(user == UsersRepo.selected.value,
                                onClick = {
                                    if (UsersRepo.selected.value != user) {
                                        UsersRepo.selected.value = user
                                        onUserClick(user)
                                    }
                                }),
                        contactState = contactState
                    )
                }
            }

        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = usersLazyListState
            )
        )
    }
}