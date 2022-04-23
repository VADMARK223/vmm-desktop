package view.right

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import db.User
import repository.UsersRepo

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Right(selectedUser: MutableState<User?>, usersRepo: UsersRepo, modifier: Modifier, onUserClick: (User) -> Unit) {
    Column (modifier = Modifier.width(300.dp)){
        Top(repo = usersRepo)
        Users(
            selectedUser,
            usersRepo = usersRepo,
            modifier = modifier,
            onUserClick = onUserClick
        )
    }
}