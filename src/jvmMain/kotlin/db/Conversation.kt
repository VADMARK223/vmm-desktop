package db

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import model.User

@Serializable
data class Conversation(
    val id: Long,
    var name: String,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val ownerId: Long,
    val companionId: Long? = null,
    val membersCount: Int,
    val lastMessage: Message? = null,
    var companion: User? = null
)
