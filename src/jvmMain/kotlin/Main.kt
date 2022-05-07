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

@Composable
@Preview
fun App(conversationsRepo: ConversationsRepo, usersRepo: UsersRepo) {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }

    val messagesRepo = MessagesRepoImpl()

    MaterialTheme(colors = darkThemeColors) {
        val contactState = remember { mutableStateOf(ContactState.HIDE) }

        /*if (UsersRepo.selected.value == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(14, 22, 33))
            ) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        contactState.value = ContactState.CREATE
                    },
                ) {
                    Text("Create contact")
                }
            }
        } else {*/
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
                InputMessage(messagesRepo, conversationsRepo, mainOutput)
            }
        }
//        }

        if (contactState.value != ContactState.HIDE) {
            Contact(contactState)
        }

        if (conversationsRepo.selected().value != null) {
            messagesRepo.messagesByConversationId(conversationsRepo.selected().value?.id as Long)
        }
    }
}

suspend fun main() = coroutineScope {
    HttpService.coroutineScope = this//rememberCoroutineScope()
    val conversationsRepo = ConversationsRepoImpl()
    val usersRepo = UsersRepoImpl()

    launch {
        initConversationsWebSocket(conversationsRepo)
//        initConversationsWebSocket()
//        initWebSocket(conversationsRepo)
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
            App(conversationsRepo, usersRepo)
        }
    }
}

suspend fun initConversationsWebSocket(conversationsRepo: ConversationsRepo) {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(
            host = "localhost",
            port = 8888,
            path = "/conversations"
        ) {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val incomingMessage = message.readText()
                println("Incoming conversations: $incomingMessage")

                val conversationNotification = withContext(Dispatchers.IO) {
                    defaultMapper.decodeFromString<ConversationNotification>(incomingMessage)
                }
                println("Conversation notification: $conversationNotification")

                if (conversationNotification.type == ChangeType.DELETE) {
                    conversationsRepo.remove(conversationNotification.id)
                }
            }
        }
    }

    client.close()
}

/*suspend fun initConversationsWebSocket() {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(
            host = "localhost",
            port = 8888,
            path = "/conversations"
        ) {
            for (message in incoming) {
                message as? Frame.Text ?: continue
                val incomingMessage = message.readText()
                println("Incoming message: $incomingMessage")

                val value = withContext(Dispatchers.IO) {
                    defaultMapper.decodeFromString<ConversationNotification>(incomingMessage)
                }

                println("Value: $value")
            }
        }
    }

    client.close()
}*/

suspend fun initWebSocket(conversationsRepo: ConversationsRepo) {
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
            val messageOutputRoutine = launch { outputMessages(conversationsRepo) }
            val userInputRoutine = launch { inputMessages() }
            userInputRoutine.join() // Wait for completion; either "exit" or error
            messageOutputRoutine.cancelAndJoin()
        }
    }
    client.close()
}

suspend fun DefaultClientWebSocketSession.outputMessages(conversationsRepo: ConversationsRepo) {
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
                if (command == "REMOVE_CONVERSATION") {
                    conversationsRepo.remove(id)
                } else if (command == "ADD_CONVERSATION") {
                    conversationsRepo.create()
                }
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
