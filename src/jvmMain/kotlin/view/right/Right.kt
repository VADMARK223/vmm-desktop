package view.right

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import repository.ConversationsRepo
import repository.MessagesRepo
import service.printDraw
import view.right.info.Info

/**
 * @author Markitanov Vadim
 * @since 21.05.2022
 */
@Composable
fun Right(
    conversationsRepo: ConversationsRepo,
    messagesRepo: MessagesRepo,
    mainOutput: MutableState<TextFieldValue>
) {
    printDraw()
    if (conversationsRepo.selected().value != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Info(conversationsRepo = conversationsRepo)

            Messages(
                Modifier
                    .weight(1f)
                    .background(color = Color(14, 22, 33)),
                mainOutput,
                messagesRepo,
                conversationsRepo
            )

            InputMessage(messagesRepo, conversationsRepo, mainOutput)
        }
    } else {
        Box(Modifier.fillMaxSize().background(color = Color(14, 22, 33))) {
            Text("Select conversation.", Modifier.align(Alignment.Center), Color.White)
        }
    }
}