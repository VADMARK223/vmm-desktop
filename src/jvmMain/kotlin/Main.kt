import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import common.ConversationsRepo
import common.MessagesRepo
import common.User
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
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
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
import java.awt.Cursor

private fun Modifier.cursorForHorizontalResize(): Modifier =
    pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
fun MainScreen() {
    printDraw()

    MaterialTheme(colors = darkThemeColors) {
        HorizontalSplitPane {
            first(450.dp) {
                Left()
            }
            second(250.dp) {
                Right()
            }
            splitter {
                visiblePart {
                    Box(
                        Modifier
                            .width(1.dp)
                            .fillMaxHeight()
                            .background(MaterialTheme.colors.background)
                    )
                }
                handle {
                    Box(
                        Modifier
                            .markAsHandle()
                            .cursorForHorizontalResize()
                            .background(SolidColor(Color.Gray), alpha = 0.50f)
                            .width(1.dp)
                            .fillMaxHeight()
                    )
                }
            }
        }

        if (UsersRepo.current().value == null) {
            Window.show(WindowType.SELECT_CURRENT_USER)
        } else {
            if (Window.state.value == WindowState(WindowType.SELECT_CURRENT_USER)) {
                Window.hide()
            }
        }

        BaseWindow()
    }
}

suspend fun main() = coroutineScope {
    HttpService.coroutineScope = this

    application {
        val icon = painterResource("favicon.ico")
        val width = 1200.dp
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

        val currentUser = UsersRepo.current().value
        if (currentUser != null) {
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
                    UsersRepo.update(userNotification.entity as User)
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
                        ChangeType.UPDATE -> ConversationsRepo.update(conversationNotification.entity)

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