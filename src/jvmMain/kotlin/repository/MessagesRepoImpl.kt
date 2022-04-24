package repository

import androidx.compose.runtime.mutableStateListOf
import db.Message
import db.Messages
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class MessagesRepoImpl : MessagesRepo {
    private val messages = mutableStateListOf<Message>()
    private val userMessageMap = mutableMapOf<Long, List<Message>>()

    init {
        if (UsersRepo.selected.value != null) {
            val selectedUserId = UsersRepo.selected.value?.id?.value as Long

            transaction {
                val messagesByUserId = Message.find { Messages.userId eq selectedUserId }
                messages.addAll(messagesByUserId)
            }
        }
    }

    override fun messageList(): List<Message> {
        return messages
    }

    override fun addMessage(textOut: String) {
        if (textOut.isEmpty()) return

        val selectedUserId = UsersRepo.selected.value?.id?.value as Long

        transaction {
            val newMessage = Message.new {
                userId = selectedUserId
                text = textOut
                isMy = true
            }
            messages.add(newMessage)
        }
    }

    override fun updateMessagesByUserId(userId: Long) {
        clearMessages()
        userMessageMap[userId]?.let { messages.addAll(it) }
    }

    private fun clearMessages() {
        messages.clear()
    }
}