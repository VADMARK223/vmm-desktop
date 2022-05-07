package view.left

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import repository.ConversationsRepo
import repository.UsersRepo
import view.common.ContactState

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Left(
    contactState: MutableState<ContactState>,
    repo: ConversationsRepo,
    usersRepo: UsersRepo
) {
    Column(modifier = Modifier.width(450.dp)) {
        Top(contactState, repo, usersRepo)
        Conversations(
            repo = repo
        )
    }
}