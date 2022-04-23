package repository

import androidx.compose.runtime.mutableStateListOf
import data.User
import java.sql.Timestamp
import java.time.Instant
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class UsersRepoImpl : UsersRepo {
    private val list = mutableStateListOf<User>()

    override fun addUser(id: Long, firstName: String, lastName: String, activityTime: Instant) {
        list.add(User(id, firstName, lastName, Random.nextInt(0, 5), activityTime))
    }

    override fun items(): List<User> {
        return list
    }

    override fun getFirst(): User {
        return list.first()
    }
}