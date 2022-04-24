package repository

import db.Message

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
interface MessagesRepo {
    fun messageList(): List<Message>
    fun addMessage(textOut: String)
    fun updateMessagesByUserId(userId: Long)
}