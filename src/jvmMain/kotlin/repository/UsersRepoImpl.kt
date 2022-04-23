package repository

import androidx.compose.runtime.mutableStateListOf
import db.User

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class UsersRepoImpl : UsersRepo {
    private val list = mutableStateListOf<User>()

    override fun addUser(user: User) {
        list.add(user)
    }

    override fun items(): List<User> {
        return list
    }

    override fun getFirst(): User {
        return list.first()
    }
}