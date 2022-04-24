package db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Markitanov Vadim
 * @since 24.04.2022
 */
class Message(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, Message>(Messages)

    var userId by Messages.userId
    var text by Messages.text
    var isMy by Messages.isMy
    var currentTime by Messages.createTime
    var edited by Messages.edited

    override fun toString(): String {
        return "Message(userId=$userId, text='$text')"
    }
}