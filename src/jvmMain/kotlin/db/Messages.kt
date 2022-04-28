package db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

/**
 * @author Markitanov Vadim
 * @since 24.04.2022
 */
object Messages : LongIdTable("messages") {
    val conversationId = long("conversation_id")
    val text = text("text")
    val isMy = bool("is_my")
    val createTime = timestamp("create_time")
    val edited = bool("edited")
}