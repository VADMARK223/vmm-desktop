package db

import kotlinx.serialization.Contextual
import java.time.LocalDateTime

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
//    @Contextual
//    val createTime: LocalDateTime
)