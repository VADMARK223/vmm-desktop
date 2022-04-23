package data

import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
data class Message(val id: Int, val text: String, val isMy: Boolean = Random.nextBoolean())