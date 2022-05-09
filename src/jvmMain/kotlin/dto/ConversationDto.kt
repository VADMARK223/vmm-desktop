package dto

@kotlinx.serialization.Serializable
data class ConversationDto(val name: String, val ownerId: Long?, val memberIds: List<Long>)
