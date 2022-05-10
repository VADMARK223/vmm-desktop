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
    fun selected(): MutableState<Conversation?>
    fun remove(conversation: Conversation)
    fun remove(conversationId: Long)
    fun create(name:String, ownerId:Long?, memberUsers: List<User>, isPrivate: Boolean)
    fun create(entity: Conversation?)
}