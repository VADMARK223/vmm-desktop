package repository

import androidx.compose.runtime.mutableStateListOf
import db.User
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class UsersRepoImpl : UsersRepo {
    private val list = mutableStateListOf<User>()

    init {
        transaction {
            addLogger(StdOutSqlLogger)

            list.addAll(User.all())
        }
    }

    override fun addUser(user: User) {
        list.add(user)
    }

    override fun items(): List<User> {
        return list
    }

    override fun getFirst(): User? {
        return if (!list.isEmpty()) list.first() else null
    }

    override fun addAll(users: SizedIterable<User>) {
        list.addAll(users)
    }
}