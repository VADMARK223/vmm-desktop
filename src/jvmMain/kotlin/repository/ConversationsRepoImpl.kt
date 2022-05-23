package repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import dto.ConversationDto
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import model.Conversation
import model.Message
import model.User
import service.HttpService

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
class ConversationsRepoImpl : ConversationsRepo {
    private val selected = mutableStateOf<Conversation?>(null)
    private var conversations = mutableStateListOf<Conversation>()

    override fun all(): List<Conversation> = conversations

    override fun updateByUserId(userId: Long) {
        println("Get conversations by user id: $userId")
        conversations.clear()
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/conversations/$userId")
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

    override fun selected(): MutableState<Conversation?> = selected

    override fun put(name: String, ownerId: Long?, memberUsers: List<User>, companionId: Long?) {
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

    override fun addAndSelect(entity: Conversation?) {
        if (entity != null) {
            conversations.add(entity)
            selected.value = entity
        }
    }

    override fun delete(conversation: Conversation) {
        HttpService.coroutineScope.launch {
            HttpService.client.delete("${HttpService.host}/conversations/${conversation.id}")
        }
    }

    override fun removeAndSelectFirst(conversationId: Long) {
        for (conversation in conversations) {
            if (conversation.id == conversationId) {
                conversations.remove(conversation)
            }
        }

        selectedFirst()
    }

    override fun updateCompanion(entity: User?) {
        for (conversation in conversations) {
            if (conversation.companion != null) {
                if (conversation.companion!!.id == entity?.id) {
                    conversation.companion = entity
                }
            }
        }
    }

    override fun updateLastMessage(conversationId: Long, message: Message?) {
        for (conversation in conversations) {
            if (conversation.id == conversationId) {
                conversations[conversations.indexOf(conversation)] = conversation.copy(lastMessage = message)
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