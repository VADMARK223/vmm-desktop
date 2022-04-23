package repository

import data.User

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
interface UsersRepo {
    fun addUser(firstName: String, lastName: String)
    fun addUser(id: Long, firstName: String, lastName: String)
    fun items(): List<User>
    fun getFirst(): User
    fun createUser(id: Long, firstName: String, lastName: String): User
}