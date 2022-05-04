import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import io.ktor.websocket.*
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import repository.ConversationsRepoImpl
import repository.MessagesRepoImpl
import resources.darkThemeColors
import service.HttpService
import view.common.Contact
import view.common.ContactState
import view.left.Left
import view.right.InputMessage
import view.right.Messages
import view.right.info.Info

@Composable
@Preview
fun App() {
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }

    HttpService.coroutineScope = rememberCoroutineScope()

    val conversationsRepo = ConversationsRepoImpl()
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
                repo = conversationsRepo
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
                    repo = messagesRepo
                )
                InputMessage(messagesRepo, conversationsRepo, mainOutput)
            }
        }
//        }

        if (contactState.value != ContactState.HIDE) {
            Contact(contactState)
        }

        if (conversationsRepo.selected().value != null) {
            println("Conservation selected: ${conversationsRepo.selected().value?.id}")
            messagesRepo.messagesByConversationId(conversationsRepo.selected().value?.id as Long)
        } else {
            println("Conservation not selected.")
        }
    }
}

suspend fun main() = coroutineScope {
    launch {
        initWebSocket()
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

suspend fun initWebSocket() {
    val client = HttpClient(CIO) {
        install(WebSockets)
    }
    runBlocking {
        client.webSocket(host = "localhost", port = 8888, path = "/chat") {
//            send("aaaaa")
            val messageOutputRoutine = launch { outputMessages() }
//            val userInputRoutine = launch { inputMessages() }
//            userInputRoutine.join() // Wait for completion; either "exit" or error
//            messageOutputRoutine.cancelAndJoin()
        }
    }
    client.close()
}

suspend fun DefaultClientWebSocketSession.outputMessages() {
    try {
        for (message in incoming) {
            message as? Frame.Text ?: continue
            println(message.readText())
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
