package db

import kotlinx.datetime.*

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@kotlinx.serialization.Serializable
data class MessageNew(
    val id: Long,
    val text: String = "Unknown",
    val isMy: Boolean = false,
    val edited: Boolean = false,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
)