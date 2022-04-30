package repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import db.Conversation
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import service.HttpService

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
class ConversationsRepoImpl : ConversationsRepo {
    private val selected = mutableStateOf<Conversation?>(null)
    private var conversations = mutableStateListOf<Conversation>()

    init {
        println("Conversations repo init.")

        conversations.clear()
        HttpService.coroutineScope.launch {
            val responseConversations =
                HttpService.client.get("${HttpService.host}/conversations").call.body<List<Conversation>>()
            println("Response conversations size: " + responseConversations.size)
            conversations.addAll(responseConversations)
            if (!conversations.isEmpty()) {
                selected.value = conversations.first()
            }
        }
    }

    override fun all(): List<Conversation> {
        return conversations
    }

    override fun selected(): MutableState<Conversation?> {
        return selected
    }
}