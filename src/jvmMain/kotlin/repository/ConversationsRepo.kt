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
}