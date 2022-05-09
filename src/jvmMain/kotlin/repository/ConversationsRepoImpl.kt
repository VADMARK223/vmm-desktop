package repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import db.Conversation
import dto.ConversationDto
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
        conversations.clear()
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/conversations")

            try {
                if (response.status == HttpStatusCode.OK) {
                    val responseConversations = response.body<List<Conversation>>()
                    conversations.addAll(responseConversations)
                    selectedFirst()
                } else {
                    val responseData = response.body<String>()
                    println(responseData)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
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

    override fun remove(conversationId: Long) {
        HttpService.coroutineScope.launch {
            val response = HttpService.client.delete("${HttpService.host}/conversations/${conversationId}")
            if (response.status == HttpStatusCode.OK) {
                for (conversation in conversations) {
                    if (conversation.id == conversationId) {
                        conversations.remove(conversation)
                    }
                }

                selectedFirst()
            }
        }
    }

    override fun create() {
//        create("Conversation #" + Random.nextInt(100), 1L)
    }

    override fun create(name:String, ownerId:Long?, memberIds: List<Long>) {
        HttpService.coroutineScope.launch {
            val conversationDto = ConversationDto(name, ownerId, memberIds)
            val response = HttpService.client.put("${HttpService.host}/conversations") {
                contentType(ContentType.Application.Json)
                setBody(conversationDto)
            }

            println("Create conversation response: $response")

            /*if (response.status == HttpStatusCode.OK) {
                val newConversation = response.body<Conversation>()
                conversations.add(newConversation)
                selected.value = newConversation
            }*/
        }
    }

    override fun test() {
        remove(30)
    }

    private fun selectedFirst() {
        if (!conversations.isEmpty()) {
            selected.value = conversations.first()
        } else {
            selected.value = null
        }
    }
}