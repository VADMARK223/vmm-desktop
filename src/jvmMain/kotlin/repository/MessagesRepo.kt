package repository

import model.Message

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
interface MessagesRepo {
    fun currentMessages(): List<Message>
    fun put(text: String, conversationId: Long?, currentUserId: Long?)
    fun delete(id: Long)
    fun messagesByConversationId(id: Long)
    fun getById(messageId: Long?): Message?
    fun addMessage(message: Message?)
    fun deleteMessage(message: Message?)
}