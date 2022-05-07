package repository

import androidx.compose.runtime.MutableState
import db.Conversation

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
interface ConversationsRepo {
    fun all(): List<Conversation>
    fun selected(): MutableState<Conversation?>
    fun remove(conversation: Conversation)
    fun remove(conversationId: Long)
    fun create()
    fun create(name:String, ownerId:Long?)
    fun test()
}