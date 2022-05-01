package repository

import db.Message

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
interface MessagesRepo {
    fun all(): List<Message>
    fun delete(message: Message)
    fun messagesByConversationId(id: Long)
    fun addMessage(text: String, conversationId: Long?)
}