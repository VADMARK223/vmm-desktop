package repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import db.Conversation
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
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
            val response = HttpService.client.get("${HttpService.host}/conversations")

            if (response.status == HttpStatusCode.OK) {
                val responseConversations = response.body<List<Conversation>>()
                conversations.addAll(responseConversations)
                selectedFirst()
            } else {
                val responseData = response.body<String>()
                println(responseData)
            }
        }
    }

    override fun all(): List<Conversation> {
        return conversations
    }

    override fun selected(): MutableState<Conversation?> {
        return selected
    }

    override fun remove(conversation: Conversation) {
        HttpService.coroutineScope.launch {
            HttpService.client.delete("${HttpService.host}/conversations/${conversation.id}")
            conversations.remove(conversation)
        }
    }

    override fun create() {
        println("Create conversation.")
        HttpService.coroutineScope.launch {
            val response = HttpService.client.put("${HttpService.host}/conversations")
            if (response.status == HttpStatusCode.OK) {
                val newConversation = response.body<Conversation>()
                println("New conversation: $newConversation")
                conversations.add(newConversation)
                selectedFirst()
            }
        }
    }

    private fun selectedFirst() {
        if (!conversations.isEmpty()) {
            selected.value = conversations.first()
        }
    }
}