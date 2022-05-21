package model

/**
 * @author Markitanov Vadim
 * @since 06.05.2022
 */
enum class ChangeType { CREATE, UPDATE, DELETE, ADD_MESSAGE, DELETE_MESSAGE, UPDATE_LAST_MESSAGE }

@kotlinx.serialization.Serializable
data class Notification<T>(
    val type: ChangeType,
    val id: Long,
    val entity: T,
    val message: Message? = null
)

typealias ConversationNotification = Notification<Conversation?>
typealias UserNotification = Notification<User?>