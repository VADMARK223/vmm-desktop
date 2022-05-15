package repository

import androidx.compose.runtime.MutableState
import db.Conversation
import model.User

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
interface ConversationsRepo {
    fun all(): List<Conversation>
    fun updateByUserId(userId: Long)
    fun selected(): MutableState<Conversation?>
    fun put(name: String, ownerId: Long?, memberUsers: List<User>, companionId: Long?)
    fun addAndSelect(entity: Conversation?)

    fun delete(conversation: Conversation)
    fun removeAndSelectFirst(conversationId: Long)
//    fun remove(conversationId: Long)
}