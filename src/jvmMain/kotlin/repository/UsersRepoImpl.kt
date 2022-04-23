package repository

import androidx.compose.runtime.mutableStateListOf
import data.User
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class UsersRepoImpl : UsersRepo {
    private val list = mutableStateListOf<User>()

    override fun addUser(firstName: String, lastName: String) {
        list.add(createUser(list.size.toLong(), firstName, lastName))
    }

    override fun addUser(id:Long, firstName: String, lastName: String) {
        list.add(createUser(id, firstName, lastName))
    }

    override fun items(): List<User> {
        return list
    }

    override fun getFirst(): User {
        return list.first()
    }

    override fun createUser(id: Long, firstName: String, lastName: String): User {
        return User(id, firstName, lastName, Random.nextInt(0, 5))
    }
}