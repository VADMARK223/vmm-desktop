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
//    fun getMessagesByConversationId(id: Long): List<Message>
    fun putMessage(text: String, conversationId: Long?, currentUserId: Long?)
    fun getById(messageId: Long?): Message?
    fun addMessage(message: Message?)
}