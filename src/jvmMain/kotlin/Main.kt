import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import repository.MessagesRepo
import repository.MessagesRepoImpl
import repository.UsersRepo
import resources.darkThemeColors
import service.databaseConnect
import view.common.NewContact
import view.left.Left
import view.right.InputMessage
import view.right.Messages
import view.right.UserInfo

@Composable
@Preview
fun App() {
    databaseConnect()
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }

    MaterialTheme(colors = darkThemeColors) {
        val newContactShow = remember { mutableStateOf(false) }
        val messagesRepo: MessagesRepo = MessagesRepoImpl()

        if (UsersRepo.selected.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(14, 22, 33))
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        newContactShow.value = true
                    },
                ) {
                    Text("Create contact")
                }
            }
        } else {
            Row {
                Left(
                    modifier = Modifier
                        .background(Color(14, 22, 33))
                        .fillMaxHeight(),
                    onUserClick = { user ->
                        messagesRepo.updateMessagesByUserId(user.id.value)
                    },
                    newContactShow = newContactShow
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    UserInfo()
                    Messages(
                        Modifier
                            .weight(1f)
                            .background(color = Color(14, 22, 33)),
                        messagesRepo,
                        mainOutput
                    )
                    InputMessage(messagesRepo, mainOutput)
                }
            }
        }

        if (newContactShow.value) {
            NewContact(newContactShow)
        }
    }
}

fun main() = application {
    val icon = painterResource("favicon.ico")
    val width = 1000.dp
    val height = 700.dp
    val state = rememberWindowState(
        size = DpSize(width, height),
        position = WindowPosition(2300.dp, 300.dp),
        isMinimized = false
    )

    Tray(
        icon = icon,
        menu = {
            Item("Close app", onClick = ::exitApplication)
        }
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        true,
        "Vadmark`s messenger",
        icon = icon
    ) {
        App()
    }
}