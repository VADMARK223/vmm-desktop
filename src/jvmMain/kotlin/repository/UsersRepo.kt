package repository

import data.User
import java.time.Instant

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
interface UsersRepo {
    fun addUser(id: Long, firstName: String, lastName: String, activityTime: Instant)
    fun items(): List<User>
    fun getFirst(): User
}