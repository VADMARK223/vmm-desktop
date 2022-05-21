package view.right

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import repository.ConversationsRepo
import repository.MessagesRepo
import repository.UsersRepo
import view.right.info.Info

/**
 * @author Markitanov Vadim
 * @since 21.05.2022
 */
@Composable
fun Right(
    conversationsRepo: ConversationsRepo,
    usersRepo: UsersRepo,
    messagesRepo: MessagesRepo,
    mainOutput: MutableState<TextFieldValue>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Info(conversationsRepo = conversationsRepo, usersRepo = usersRepo)

        Messages(
            Modifier
                .weight(1f)
                .background(color = Color(14, 22, 33)),
            mainOutput,
            messagesRepo,
            usersRepo,
            conversationsRepo
        )

        InputMessage(messagesRepo, conversationsRepo, mainOutput, usersRepo)
    }
}