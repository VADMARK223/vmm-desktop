package repository

import androidx.compose.runtime.mutableStateListOf
import db.MessageNew
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
class MessagesRepoNewImpl : MessagesRepoNew {
    private val messages = mutableStateListOf<MessageNew>()

    init {
        HttpService.coroutineScope.launch {
            messages.clear()
            val responseMessages =
                HttpService.client.get("${HttpService.host}/messages/conversation/2").call.body<List<MessageNew>>()
            println("Response messages size: " + responseMessages.size)
            messages.addAll(responseMessages)
        }
    }

    override fun all(): List<MessageNew> {
        return messages
    }

    override fun delete(message: MessageNew) {
        HttpService.coroutineScope.launch {
            HttpService.client.delete("${HttpService.host}/messages/${message.id}")
            messages.remove(message)
        }
        messages.remove(message)
    }

    override fun messagesByConversationId(id: Long) {
        messages.clear()
        HttpService.coroutineScope.launch {
            val responseMessages =
                HttpService.client.get("${HttpService.host}/messages/conversation/${id}").call.body<List<MessageNew>>()
            messages.addAll(responseMessages)
        }
    }

    override fun addMessage(text: String, conversationId: Long?) {
        val messageDto = MessageDto(text = text, conversationId = conversationId)
        println("Add message: $messageDto")

        HttpService.coroutineScope.launch {
            val response = HttpService.client.post("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(messageDto)
            }
            println("Response: $response")
            if (response.status == HttpStatusCode.OK) {
                val newMessage = response.body<MessageNew>()
                println("New message: $newMessage")
                messages.add(newMessage)
            }
        }
    }
}

@Serializable
data class MessageDto(val text: String, val conversationId: Long?)