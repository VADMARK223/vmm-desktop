package common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
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

/**
 * @author Markitanov Vadim
 * @since 23.05.2022
 */
@Serializable
data class Conversation(
    val id: Long,
    var name: String,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val ownerId: Long,
    val companionId: Long? = null,
    val membersCount: Int? = null,
    var lastMessage: Message? = null,
    var companion: User? = null
) {
    val visibleName: MutableState<String> = mutableStateOf(name)
    val lastMessageVisible: MutableState<Message?> = mutableStateOf(lastMessage)
}

object ConversationsRepo {
    private val selected = mutableStateOf<Conversation?>(null)
    private var conversations = mutableStateListOf<Conversation>()

    fun all(): List<Conversation> = conversations

    fun updateByUserId(userId: Long) {
        println("Get conversations by user id: $userId")
        conversations.clear()

        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/conversations/$userId")
            try {
                println("Response conversations: $response")
                if (response.status == HttpStatusCode.OK) {
                    val responseConversations = response.body<List<Conversation>>()
                    conversations.addAll(responseConversations)
                    selectedFirst()
                    println("Conversations update and first select.")
                } else {
                    val responseData = response.body<String>()
                    println(responseData)
                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }
    }

    fun selected(): MutableState<Conversation?> = selected

    fun put(name: String, ownerId: Long?, memberUsers: List<User>, companionId: Long?) {
        HttpService.coroutineScope.launch {
            val memberIds = mutableListOf<Long>()
            for (user in memberUsers) {
                memberIds.add(user.id)
            }
            HttpService.client.put("${HttpService.host}/conversations") {
                contentType(ContentType.Application.Json)
                setBody(ConversationDto(name, ownerId, memberIds, companionId))
            }
        }
    }

    fun addAndSelect(entity: Conversation?) {
        if (entity != null) {
            conversations.add(entity)
            selected.value = entity
        }
    }

    fun delete(conversation: Conversation) {
        HttpService.coroutineScope.launch {
            HttpService.client.delete("${HttpService.host}/conversations/${conversation.id}")
        }
    }

    fun removeAndSelectFirst(conversationId: Long) {
        for (conversation in conversations) {
            if (conversation.id == conversationId) {
                conversations.remove(conversation)
            }
        }

        selectedFirst()
    }

    fun updateCompanion(entity: User?) {
        for (conversation in conversations) {
            if (conversation.companion != null) {
                if (conversation.companion!!.id == entity?.id) {
                    conversation.companion = entity
                }
            }
        }
    }

    fun updateLastMessage(conversationId: Long, message: Message?) {
        for (conversation in conversations) {
            if (conversation.id == conversationId) {
                conversation.lastMessageVisible.value = message
            }
        }
    }

    private fun selectedFirst() {
        if (!conversations.isEmpty()) {
            selected.value = conversations.first()
        } else {
            selected.value = null
        }
    }
}

@Serializable
data class ConversationDto(
    val name: String,
    val ownerId: Long?,
    val memberIds: List<Long>,
    val companionId: Long?
)