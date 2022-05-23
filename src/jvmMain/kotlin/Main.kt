import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import common.ConversationsRepo
import common.MessagesRepo
import common.UsersRepo
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import model.ChangeType
import model.ConversationNotification
import model.UserNotification
import resources.darkThemeColors
import service.HttpService
import service.printDraw
import util.JsonMapper.defaultMapper
import view.left.Left
import view.right.Right
import view.window.*
import view.window.WindowState

@Composable
@Preview
fun App() {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }
    printDraw()

    MaterialTheme(colors = darkThemeColors) {

        Row {
            Left()
            Right(mainOutput)
        }

        if (UsersRepo.current().value == null) {
            Window.state.value = WindowState(WindowType.SELECT_CURRENT_USER)
        } else {
            if (Window.state.value == WindowState(WindowType.SELECT_CURRENT_USER)) {
                Window.state.value = WindowState(WindowType.HIDE)
            }
        }

        when (Window.state.value.type) {
            WindowType.SELECT_CURRENT_USER -> SelectCurrentUser()
            WindowType.NEW_CONVERSATION -> NewConversation()
            WindowType.NEW_PRIVATE_CONVERSATION -> NewPrivateConversation()
            WindowType.ADD_MEMBERS -> {
                val conversationName = Window.state.value.data as String
                AddMembers(conversationName)
            }
            WindowType.VIEW_PROFILE -> ViewProfile()
            WindowType.VIEW_GROUP_INFO -> ViewGroupInfo()
            else -> {}
        }
    }
}

suspend fun main() = coroutineScope {
    HttpService.coroutineScope = this

    UsersRepo.addListener { userId ->
        println("User loaded: $userId.")
        ConversationsRepo.updateByUserId(userId)
        launch {
            initUsersWebSocket(userId)
        }

        launch {
            initConversationsWebSocket(userId)
        }
    }

    application {
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
                Item("Quit vmm", onClick = ::exitApplication)
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
}

suspend fun initUsersWebSocket(userId: Long) {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(
            host = "localhost",
            port = 8888,
            path = "/users",
            request = {
                this.parameter("userId", userId)
            }
        ) {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val incomingMessage = message.readText()
                println("Incoming from users: $incomingMessage")

                val userNotification = withContext(Dispatchers.IO) {
                    defaultMapper.decodeFromString<UserNotification>(incomingMessage)
                }

                if (userNotification.type == ChangeType.UPDATE) {
                    ConversationsRepo.updateCompanion(userNotification.entity)
                    UsersRepo.update(userNotification.entity)
                }

            }
        }
    }

    client.close()
}

suspend fun initConversationsWebSocket(userId: Long) {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(
            host = "localhost",
            port = 8888,
            path = "/conversations",
            request = {
                this.parameter("userId", userId)
            }
        ) {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val incomingMessage = message.readText()
                println("Incoming from conversations: $incomingMessage")

                try {
//                    val conversationNotification = defaultMapper.decodeFromString<ConversationNotification>(incomingMessage)

                    val conversationNotification = withContext(Dispatchers.IO) {
                        defaultMapper.decodeFromString<ConversationNotification>(incomingMessage)
                    }

                    when (conversationNotification.type) {
                        ChangeType.CREATE -> ConversationsRepo.addAndSelect(conversationNotification.entity)
                        ChangeType.DELETE -> ConversationsRepo.removeAndSelectFirst(conversationNotification.id)
                        ChangeType.ADD_MESSAGE -> MessagesRepo.addMessage(conversationNotification.message)
                        ChangeType.DELETE_MESSAGE -> MessagesRepo.deleteMessage(conversationNotification.message)
                        ChangeType.UPDATE_LAST_MESSAGE -> ConversationsRepo.updateLastMessage(
                            conversationNotification.id,
                            conversationNotification.message
                        )

                        else -> {
                            println("Unknown notification type ${conversationNotification.type}.")
                        }
                    }
                } catch (e: Exception) {
                    println("Error: " + e.localizedMessage)
                }
            }
        }
    }

    client.close()
}

/*suspend fun initWebSocket() {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }
    runBlocking {
        client.webSocket(
            host = "localhost",
            port = 8888,
            path = "/chat",
            request = {
                this.parameter("userId", "000")
            }
        ) {
            val messageOutputRoutine = launch { outputMessages() }
            val userInputRoutine = launch { inputMessages() }
            userInputRoutine.join() // Wait for completion; either "exit" or error
            messageOutputRoutine.cancelAndJoin()
        }
    }
    client.close()
}

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            val incomingMessage = message.readText()
            println(incomingMessage)
            val incomingMessageArray: Array<String> = incomingMessage.split(":").toTypedArray()

            if (incomingMessageArray.size == 3) {
                val command: String = incomingMessageArray[1].trim()
                println("command: '${command}'")
                val id: Long = incomingMessageArray[2].trim().toLong()
                println("id: '${id}'")
            }
        }
    } catch (e: Exception) {
        println("Error while receiving: " + e.localizedMessage)
    }
}

suspend fun DefaultClientWebSocketSession.inputMessages() {
    while (true) {
        val message = readLine() ?: ""
        if (message.equals("exit", true)) return
        try {
            send(message)
        } catch (e: Exception) {
            println("Error while sending: " + e.localizedMessage)
            return
        }
    }
}
*/