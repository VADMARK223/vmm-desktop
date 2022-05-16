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
    private val messages = mutableStateListOf<Message>()

    /*init {
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/messages")
            if (response.status == HttpStatusCode.OK) {
                println("OK")
                val responseMessages = response.body<List<Message>>()
                messages.addAll(responseMessages)
            }
        }
    }*/

    override fun all(): List<Message> {
        return messages
    }

    override fun delete(message: Message) {
        HttpService.coroutineScope.launch {
            val response = HttpService.client.delete("${HttpService.host}/messages/${message.id}")
            if (response.status == HttpStatusCode.OK) {
                val success = response.body<Boolean>()
                if (success) {
                    messages.remove(message)
                }
            }
        }
    }

    override fun messagesByConversationId(id: Long) {
        messages.clear()
        HttpService.coroutineScope.launch {
            val responseMessages =
                HttpService.client.get("${HttpService.host}/messages/conversation/${id}").call.body<List<Message>>()
            messages.addAll(responseMessages)
        }
    }

    /*override fun getMessagesByConversationId(id: Long): List<Message> {
        println("Request messages by conversation: "  + id)
        val result = mutableStateListOf<Message>()
        HttpService.coroutineScope.launch {
            val responseMessages =
                HttpService.client.get("${HttpService.host}/messages/conversation/${id}").call.body<List<Message>>()
            println("responseMessages: ${responseMessages.size}")
            result.addAll(responseMessages)
        }

        return result
    }*/

    override fun addMessage(text: String, conversationId: Long?, currentUserId: Long?) {
        val messageDto = MessageDto(text = text, conversationId = conversationId, ownerId = currentUserId)
        println("Add message: $messageDto")

        HttpService.coroutineScope.launch {
            val response = HttpService.client.put("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(messageDto)
            }
            println("Response: $response")
            if (response.status == HttpStatusCode.OK) {
                val newMessage = response.body<Message>()
                println("New message: $newMessage")
                messages.add(newMessage)
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
}

@Serializable
data class MessageDto(val text: String, val conversationId: Long?, val ownerId: Long?)