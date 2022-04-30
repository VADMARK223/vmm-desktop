package repository

import db.MessageNew

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
interface MessagesRepoNew {
    fun all(): List<MessageNew>
    fun delete(message: MessageNew)
}