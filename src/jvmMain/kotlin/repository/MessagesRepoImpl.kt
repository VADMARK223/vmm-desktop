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
    private val conversationByMessages = mutableMapOf<Long, Collection<Message>>()
    private val messages = mutableStateListOf<Message>()

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

        if (conversationByMessages.containsKey(id)) {
            conversationByMessages[id]?.let { messages.addAll(it) }
        } else {
            HttpService.coroutineScope.launch {
                val responseMessages =
                    HttpService.client.get("${HttpService.host}/messages/conversation/${id}").call.body<List<Message>>()
                messages.addAll(responseMessages)
                conversationByMessages[id] = responseMessages
            }
        }
    }

    override fun addMessage(text: String, conversationId: Long?, currentUserId: Long?) {
        val messageDto = MessageDto(text = text, conversationId = conversationId, ownerId = currentUserId)
        println("Add message: $messageDto")

        HttpService.coroutineScope.launch {
            val response = HttpService.client.put("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(messageDto)
            }
            /*println("Response: $response")
            if (response.status == HttpStatusCode.OK) {
                val newMessage = response.body<Message>()
                println("New message: $newMessage")
                messages.add(newMessage)
            }*/
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

    override fun addMessageNew(data: String) {
        println("ADD222")
//        println("ADD MESSAGE: $data")
        messages.add(Message(1, data, 1L))
    }
}

@Serializable
data class MessageDto(val text: String, val conversationId: Long?, val ownerId: Long?)