package common

import androidx.compose.runtime.mutableStateListOf
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import service.HttpService
import java.time.format.DateTimeFormatter

/**
 * @author Markitanov Vadim
 * @since 23.05.2022
 */
@Serializable
data class Message(
    val id: Long = -1,
    val text: String = "Unknown",
    val ownerId: Long? = -1,
    val edited: Boolean = false,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val conversationId: Long? = -1,
    val file: ByteArray? = null
) {
    val isSystem: Boolean
        get() {
            return ownerId == -1L
        }
    val isMy: Boolean = ownerId == UsersRepo.current().value?.id
    override fun toString(): String {
        return "Message(id=${id}, text=${text})"
    }
}

object MessagesRepo {
    private val conversationByMessages = mutableMapOf<Long, MutableList<Message>>()
    private val messages = mutableStateListOf<Message>()

    fun currentMessages(): List<Message> {
        return messages
    }

    /*fun put(text: String, conversationId: Long?, currentUserId: Long?, message: Message? = null) {
        val messageDto = MessageDto(id, text, conversationId, currentUserId, isEdited)
        println("Put message: $messageDto")

        HttpService.coroutineScope.launch {
            HttpService.client.put("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(messageDto)
            }
        }
    }*/

    fun put(text: String, file: ByteArray? = null) {
        val conversationSelectedId = ConversationsRepo.selected().value?.id
        val currentUserId = UsersRepo.current().value?.id

        put(
            Message(
                text = text,
                conversationId = conversationSelectedId,
                ownerId = currentUserId,
                file = file
            )
        )
    }

    fun put(message: Message) {
        println("Put message: $message")

        HttpService.coroutineScope.launch {
            HttpService.client.put("${HttpService.host}/messages") {
                contentType(ContentType.Application.Json)
                setBody(message)
            }
        }
    }

    fun delete(id: Long) {
        HttpService.coroutineScope.launch {
            HttpService.client.delete("${HttpService.host}/messages/${id}")
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

                val formatter = DateTimeFormatter.ofPattern("dd.MM")
                val newMessages = mutableListOf<Message>()
                for (message in responseMessages) {
                    val currentIndex = responseMessages.indexOf(message)
                    val prevMessage = responseMessages.elementAtOrNull(currentIndex - 1)
                    if (prevMessage == null) {
                        newMessages.add(Message(
                            text = formatter.format(message.createTime.toJavaLocalDateTime())
                        ))
                    } else {
                        if (message.createTime.date.dayOfYear != prevMessage.createTime.date.dayOfYear) {
                            newMessages.add(Message(
                                text = formatter.format(message.createTime.toJavaLocalDateTime())
                            ))
                        }
                    }

//                    val currentMessage = responseMessages.get(responseMessages.indexOf(message))

                    newMessages.add(message)
                }

                conversationByMessages[id] = newMessages
                messages.addAll(newMessages)
            }
        }
    }

    private fun getById(messageId: Long?): Message? = messages.singleOrNull { it.id == messageId }

    fun addMessage(message: Message?) {
        if (message != null) {
            if (message.edited) {
                val mes = getById(message.id)
                messages[messages.indexOf(mes)] = message

                if (conversationByMessages.containsKey(message.conversationId)) {
                    conversationByMessages[message.conversationId]?.let {
                        conversationByMessages[message.conversationId]?.set(
                            it.indexOf(mes), message
                        )
                    }
                }
            } else {
                if (conversationByMessages.containsKey(message.conversationId)) {
                    conversationByMessages[message.conversationId]?.add(message)
                }
                messages.add(message)
            }
        }
    }

    fun deleteMessage(message: Message?) {
        if (message != null) {
            val currentIndex = messages.indexOf(message)
            val prevMessage = messages.elementAtOrNull(currentIndex - 1)
            if (prevMessage != null && prevMessage.ownerId == -1L) {
                messages.remove(prevMessage)
            }

            if (conversationByMessages.containsKey(message.conversationId)) {
                conversationByMessages[message.conversationId]?.remove(message)
                if (prevMessage != null && prevMessage.ownerId == -1L) {
                    conversationByMessages[message.conversationId]?.remove(prevMessage)
                }
            }
            messages.remove(message)
        }
    }
}