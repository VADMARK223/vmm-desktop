package repository

import androidx.compose.runtime.mutableStateListOf
import db.MessageNew

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
class MessagesRepoNewImpl : MessagesRepoNew {
    private val messages = mutableStateListOf<MessageNew>()

    init {
        messages.add(MessageNew(1))
        messages.add(MessageNew(2))
        messages.add(MessageNew(3))
    }

    override fun all(): List<MessageNew> {
        return messages
    }
}