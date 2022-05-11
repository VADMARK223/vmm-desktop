import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import model.ChangeType
import model.ConversationNotification
import repository.*
import resources.darkThemeColors
import service.HttpService
import util.JsonMapper.defaultMapper
import view.common.Contact
import view.common.ContactState
import view.left.Left
import view.right.InputMessage
import view.right.Messages
import view.right.info.Info
import view.window.*
import view.window.WindowState

@Composable
@Preview
fun App(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }
    val conversationWebSocketInit = remember { mutableStateOf(false) }
    val messagesRepo = MessagesRepoImpl()

    MaterialTheme(colors = darkThemeColors) {
        val contactState = remember { mutableStateOf(ContactState.HIDE) }

        Row {
            Left(
                contactState = contactState,
                repo = conversationsRepo,
                usersRepo = usersRepo
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Info(conversation = conversationsRepo.selected().value, contactState = contactState)
                Messages(
                    Modifier
                        .weight(1f)
                        .background(color = Color(14, 22, 33)),
                    mainOutput,
                    repo = messagesRepo,
                    usersRepo = usersRepo
                )
                InputMessage(messagesRepo, conversationsRepo, mainOutput, usersRepo)
            }
        }

        if (contactState.value != ContactState.HIDE) {
            Contact(contactState)
        }

        if (conversationsRepo.selected().value != null) {
            messagesRepo.messagesByConversationId(conversationsRepo.selected().value?.id as Long)
        }

        if (usersRepo.current().value == null) {
            Window.state.value = WindowState(WindowType.SELECT_CURRENT_USER)
        } else {
            if (!conversationWebSocketInit.value) {
                HttpService.coroutineScope.launch {
                    initConversationsWebSocket(conversationsRepo, usersRepo)
                }
                conversationWebSocketInit.value = true
            }
        }

        when (Window.state.value.type) {
            WindowType.SELECT_CURRENT_USER -> SelectCurrentUser(usersRepo)
            WindowType.NEW_CONVERSATION -> NewConversation()
            WindowType.NEW_PRIVATE_CONVERSATION -> NewPrivateConversation(conversationsRepo, usersRepo)
            WindowType.ADD_MEMBERS -> {
                val conversationName = Window.state.value.data as String
                AddMembers(conversationsRepo, usersRepo, conversationName)
            }
            else -> {}
        }
    }
}

suspend fun main() = coroutineScope {
    HttpService.coroutineScope = this//rememberCoroutineScope()
    val conversationsRepo = ConversationsRepoImpl()
    val usersRepo = UsersRepoImpl()

//    launch {
//        initConversationsWebSocket(conversationsRepo, usersRepo)
//    }

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
            App(conversationsRepo, usersRepo)
        }
    }
}

suspend fun initConversationsWebSocket(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(
            host = "localhost",
            port = 8888,
            path = "/conversations",
            request = {
                this.parameter("userId", usersRepo.current().value?.id)
            }
        ) {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val incomingMessage = message.readText()
                println("Incoming from conversations: $incomingMessage")

                val conversationNotification = withContext(Dispatchers.IO) {
                    defaultMapper.decodeFromString<ConversationNotification>(incomingMessage)
                }

                when (conversationNotification.type) {
                    ChangeType.CREATE -> {
                        conversationsRepo.addAndSelect(conversationNotification.entity)
                    }
                    ChangeType.DELETE -> conversationsRepo.removeAndSelectFirst(conversationNotification.id)
                    else -> {
                        println("Unknown type ${conversationNotification.type}.")
                    }
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