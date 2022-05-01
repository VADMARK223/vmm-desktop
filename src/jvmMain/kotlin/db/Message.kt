package db

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 29.04.2022
 */
@Serializable
data class Message(
    val id: Long = Random.nextLong(),
    val text: String = "Unknown",
    val isMy: Boolean = Random.nextBoolean(),
    val edited: Boolean = true,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val conversationId: Long = Random.nextLong()
)