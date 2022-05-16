package view.left

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import repository.ConversationsRepo
import repository.MessagesRepo
import repository.UsersRepo

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
@Composable
fun Left(
    conversationsRepo: ConversationsRepo,
    usersRepo: UsersRepo,
    messagesRepo: MessagesRepo
) {
    Column(modifier = Modifier.width(450.dp)) {
        Top(usersRepo)
        Conversations(
            conversationsRepo = conversationsRepo,
            usersRepo = usersRepo,
            messagesRepo = messagesRepo
        )
    }
}