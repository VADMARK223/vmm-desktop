package common

/**
 * @author Markitanov Vadim
 * @since 25.05.2022
 */
@kotlinx.serialization.Serializable
data class Image(val text: String, val image: ByteArray)