package repository

import db.User

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
interface UsersRepo {
    fun addUser(user: User)
    fun items(): List<User>
    fun getFirst(): User
}