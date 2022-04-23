package repository

import data.Message

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
interface MessagesRepo {
    fun messageList(): List<Message>
    fun addMessage(item: Message)
    fun clearMessages()
    fun updateMessagesByUserId(userId: Long)
    fun messagesByUserId(userId: Long): List<Message>
}