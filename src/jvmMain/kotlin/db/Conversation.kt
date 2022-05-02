package db

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Conversation(
    val id: Long,
    var name: String,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime
)
