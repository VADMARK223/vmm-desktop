package repository

import db.User
import org.jetbrains.exposed.sql.SizedIterable

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
interface UsersRepo {
    fun addUser(user: User)
    fun items(): List<User>
    fun getFirst(): User
    fun addAll(users: SizedIterable<User>)
}