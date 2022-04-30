package db

import kotlinx.datetime.*
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@kotlinx.serialization.Serializable
data class MessageNew(
    val id: Long,
    val text: String = "Unknown",
    val isMy: Boolean = Random.nextBoolean(),
    val edited: Boolean = false,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)
)