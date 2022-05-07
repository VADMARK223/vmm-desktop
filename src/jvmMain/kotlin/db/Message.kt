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
    val ownerId: Long,
    val edited: Boolean = false,
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC),
    val conversationId: Long = Random.nextLong()
)