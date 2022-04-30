package service

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import db.Conversation
import db.MessageNew
import db.TempUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.transactions.transaction

object HttpService {
    private const val host: String = "http://localhost:8888"

    lateinit var coroutineScope: CoroutineScope

    private val selectedUser = mutableStateOf<TempUser?>(null)
    val selectedConversation = mutableStateOf<Conversation?>(null)
    private var conversationList = mutableStateListOf<Conversation>()

    private val messages = mutableStateListOf<MessageNew>()

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }

    init {
        println("Init const.")
    }

    fun selectUser(coroutineScope: CoroutineScope, userId: Long) {
        coroutineScope.launch {
            val tempUser: TempUser = client.get("$host/user/${userId}").body()
            println("New selected user: $tempUser")
            selectedUser.value = tempUser
        }
    }

    fun getConversationList(): List<Conversation> {
        return conversationList
    }

    fun requestAllConversation() {
        println("Request all conversation.")
        coroutineScope.launch {
            val conversations = client.get("$host/conversations").call.body<List<Conversation>>()
            conversationList.addAll(conversations)
            selectedConversation.value = conversations.first()
            messagesById(selectedConversation.value?.id as Long)
        }
    }

    fun messagesById(conversationId: Long) {
        messages.clear()
        coroutineScope.launch {
            val messagesByConversationId =
                client.get("$host/messages/conversation/${conversationId}").call.body<List<MessageNew>>()
            messages.addAll(messagesByConversationId)
        }
    }

    fun messagesList(): List<MessageNew> {
        return messages
    }

    fun removeMessage(message: MessageNew) {
//        coroutineScope.launch {
//            client.delete("$host/messages/${message.id}")
//            messages.remove(message)
//        }
        transaction {
            messages.remove(message)
        }
    }
}