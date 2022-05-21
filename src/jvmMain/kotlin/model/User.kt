package model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val online: Boolean = false
) {
    val name: String
        get() = "$firstName $lastName"
}
