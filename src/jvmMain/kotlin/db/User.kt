package db

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class User(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, User>(Users)

    var firstName by Users.firstName
    var lastName by Users.lastName
    var activityTime by Users.activityTime
}