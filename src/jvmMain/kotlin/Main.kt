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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import db.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import repository.MessagesRepo
import repository.MessagesRepoImpl
import repository.UsersRepo
import repository.UsersRepoImpl
import resources.darkThemeColors
import view.InputMessage
import view.Messages
import view.UserInfo
import view.right.Right

@Composable
@Preview
fun App() {
    val usersRepo: UsersRepo = UsersRepoImpl()
    val database =
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/vmm",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "postgres"
        )
    transaction(database) {
        addLogger(StdOutSqlLogger)
        usersRepo.addAll(User.all())
    }

    MaterialTheme(colors = darkThemeColors) {
        val selectedUser = remember { mutableStateOf(usersRepo.getFirst()) }
        val messagesRepo: MessagesRepo = MessagesRepoImpl(usersRepo)

        if (selectedUser.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(14, 22, 33))
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        transaction {
                            addLogger(StdOutSqlLogger)
                            usersRepo.addAll(User.all())
                        }

                        selectedUser.value = usersRepo.getFirst()
                    },
                ) {
                    Text("Load users")
                }
            }
        } else {
            Row {
                Right(
                    selectedUser,
                    repo = usersRepo,
                    modifier = Modifier
                        .weight(1.0f)
                        .background(Color(14, 22, 33))
                        .fillMaxHeight(),
                    onUserClick = { user ->
                        messagesRepo.updateMessagesByUserId(user.id.value)
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2.0f)
                ) {
                    UserInfo(selectedUser)
                    Messages(
                        Modifier
                            .weight(1f)
                            .background(color = Color(14, 22, 33)),
                        messagesRepo
                    )
                    InputMessage(messagesRepo)
                }
            }
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