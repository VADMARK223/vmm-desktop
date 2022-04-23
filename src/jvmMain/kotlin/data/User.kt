package data

import java.time.Instant

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val minutesAgo: Int,
    val activityTime: Instant
)