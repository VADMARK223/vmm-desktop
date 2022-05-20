package repository

import androidx.compose.runtime.mutableStateListOf
import db.Message
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import service.HttpService

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
class MessagesRepoImpl : MessagesRepo {
    private val conversationByMessages = mutableMapOf<Long, MutableList<Message>>()
    private val messages = mutableStateListOf<Message>()

    override fun currentMessages(): List<Message> {
        return messages
    }

    override fun put(text: String, conversationId: Long?, currentUserId: Long?) {
        val messageDto = MessageDto(text = text, conversationId = conversationId, ownerId = currentUserId)
        println("Put message: $messageDto")

        HttpService.coroutineScope.launch {
            HttpService.client.put("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(messageDto)
            }
        }
    }

    override fun delete(id: Long) {
        HttpService.coroutineScope.launch {
            /*val response = */HttpService.client.delete("${HttpService.host}/messages/${id}")
            /*if (response.status == HttpStatusCode.OK) {
                val success = response.body<Boolean>()
                if (success) {
                    messages.remove(message)
                }
            }*/
        }
    }

    override fun messagesByConversationId(id: Long) {
        messages.clear()

        if (conversationByMessages.containsKey(id)) {
            conversationByMessages[id]?.let { messages.addAll(it) }
        } else {
            HttpService.coroutineScope.launch {
                val responseMessages =
                    HttpService.client.get("${HttpService.host}/messages/conversation/${id}").call.body<MutableList<Message>>()
                messages.addAll(responseMessages)
                conversationByMessages[id] = responseMessages
            }
        }
    }

    override fun getById(messageId: Long?): Message? {
        for (message in messages) {
            if (messageId == message.id) {
                return message
            }
        }

        return null
    }

    override fun addMessage(message: Message?) {
        if (message != null) {
            if (conversationByMessages.containsKey(message.conversationId)) {
                conversationByMessages[message.conversationId]?.add(message)
            }
            messages.add(message)
        }
    }

    override fun deleteMessage(message: Message?) {
        if (message != null) {
            if (conversationByMessages.containsKey(message.conversationId)) {
                conversationByMessages[message.conversationId]?.remove(message)
            }
            messages.remove(message)
        }
    }
}

@Serializable
data class MessageDto(val text: String, val conversationId: Long?, val ownerId: Long?)