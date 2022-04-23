package db

import org.jetbrains.exposed.dao.id.LongIdTable
/**
 * @author Markitanov Vadim
 * @since 24.04.2022
 */
object Messages : LongIdTable("messages") {
    val userId = long("user_id")
    val text = text("text")
    val isMy = bool("is_my")
}