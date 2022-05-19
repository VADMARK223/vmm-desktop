package model

import db.Conversation

/**
 * @author Markitanov Vadim
 * @since 06.05.2022
 */
enum class ChangeType { CREATE, UPDATE, DELETE, ADD_MESSAGE }

@kotlinx.serialization.Serializable
data class Notification<T>(val type: ChangeType, val id: Long, val entity: T, val data: String)

typealias ConversationNotification = Notification<Conversation?>
typealias UserNotification = Notification<User?>