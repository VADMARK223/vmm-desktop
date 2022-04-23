package view.left

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import db.User
import org.jetbrains.exposed.sql.transactions.transaction
import repository.UsersRepo
import view.item.user.UserItem

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Users(modifier: Modifier, onUserClick: (User) -> Unit) {
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
                            })
                )
            }
        }

    }
}