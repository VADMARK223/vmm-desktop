package model

import db.Conversation

/**
 * @author Markitanov Vadim
 * @since 06.05.2022
 */
enum class ChangeType { CREATE, UPDATE, DELETE }

@kotlinx.serialization.Serializable
data class Notification<T>(val type: ChangeType, val id: Long, val entity: T)

typealias ConversationNotification = Notification<Conversation?>
typealias UserNotification = Notification<User?>