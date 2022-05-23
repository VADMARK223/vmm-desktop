package common

import androidx.compose.runtime.mutableStateListOf
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import service.HttpService
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.05.2022
 */
@Serializable
data class Message(
    val id: Long = Random.nextLong(),
    val text: String = "Unknown",
    val ownerId: Long,
    val edited: Boolean = false,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val conversationId: Long = Random.nextLong()
)

object MessagesRepo {
    private val conversationByMessages = mutableMapOf<Long, MutableList<Message>>()
    private val messages = mutableStateListOf<Message>()

    fun currentMessages(): List<Message> {
        return messages
    }

    fun put(text: String, conversationId: Long?, currentUserId: Long?) {
        val messageDto = MessageDto(text = text, conversationId = conversationId, ownerId = currentUserId)
        println("Put message: $messageDto")

        HttpService.coroutineScope.launch {
            HttpService.client.put("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(messageDto)
            }
        }
    }

    fun delete(id: Long) {
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

    fun messagesByConversationId(id: Long) {
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

    fun getById(messageId: Long?): Message? = messages.singleOrNull { it.id == messageId }

    fun addMessage(message: Message?) {
        if (message != null) {
            if (conversationByMessages.containsKey(message.conversationId)) {
                conversationByMessages[message.conversationId]?.add(message)
            }
            messages.add(message)
        }
    }

    fun deleteMessage(message: Message?) {
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