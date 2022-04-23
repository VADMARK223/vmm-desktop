package db

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

object Users : LongIdTable("users") {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val activityTime = timestamp("activity_time")
}