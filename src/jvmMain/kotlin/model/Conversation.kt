package model

import common.User
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: Long,
    var name: String,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updateTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val ownerId: Long,
    val companionId: Long? = null,
    val membersCount: Int? = null,
    var lastMessage: Message? = null,
    var companion: User? = null
) /*{
    val visibleName: String
        get() =
            if (this.companion == null) {
                this.name
            } else {
                this.companion!!.name
            }
}*/
