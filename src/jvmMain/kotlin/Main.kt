import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toComposeImageBitmap
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
import view.window.BaseWindow
import view.window.Window
import view.window.WindowState
import view.window.WindowType
import java.awt.FileDialog
import java.io.File

@Composable
fun MainScreen() {
    printDraw()
    val mainOutput = remember { mutableStateOf(TextFieldValue("")) }

    MaterialTheme(colors = darkThemeColors) {
        Row {
            Left()
            Right(mainOutput)
        }

        if (UsersRepo.current().value == null) {
            Window.show(WindowType.SELECT_CURRENT_USER)
        } else {
            if (Window.state.value == WindowState(WindowType.SELECT_CURRENT_USER)) {
                Window.hide()
            }
        }

        BaseWindow()

        val imageFullName = remember { mutableStateOf<String?>(null) }
        if (imageFullName.value != null) {
            println("GOOD: ${imageFullName.value}")
            val file = imageFullName.value?.let { File(it) }
            if (file != null) {
                if (file.exists()) {
                    Image(
                        bitmap = org.jetbrains.skia.Image.makeFromEncoded(file.readBytes()).toComposeImageBitmap(),
                        contentDescription = "Test"
                    )
                }
            }
        }

        AwtWindow(
            true,
            create = {
                val parent : java.awt.Frame? = null
                object : FileDialog(parent, "Choose a image", LOAD) {
                    override fun setVisible(b: Boolean) {
                        super.setVisible(b)
                        if (b) {
                            println("Call close request.")
                            imageFullName.value = directory + file
                        }
                    }
                }
            },
            dispose = FileDialog::dispose
        )
    }
}

suspend fun main() = coroutineScope {
    HttpService.coroutineScope = this

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
            MainScreen()
        }

        /**/

        val currentUser = UsersRepo.current().value
        if (currentUser != null) {
            println("User loaded: $currentUser")
            ConversationsRepo.updateByUserId(currentUser.id)
            launch {
                initUsersWebSocket(currentUser.id)
            }
            launch {
                initConversationsWebSocket(currentUser.id)
            }
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