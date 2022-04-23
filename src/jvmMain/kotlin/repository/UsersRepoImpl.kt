package repository

import androidx.compose.runtime.mutableStateListOf
import db.User
import org.jetbrains.exposed.sql.SizedIterable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 23.04.2022
 */
class UsersRepoImpl : UsersRepo {
    private var list = mutableStateListOf<User>()

    init {
        transaction {
            addLogger(StdOutSqlLogger)
            list.addAll(User.all())
        }
    }

    override fun addUser() {
        transaction {
            val user = User.new {
                firstName = "FirstName#" + Random.nextInt(1000)
                lastName = "LastName#" + Random.nextInt(1000)
                activityTime = Instant.now()
            }
            list.add(user)
        }
    }

    override fun getFirst(): User? {
        return if (!list.isEmpty()) list.first() else null
    }

    override fun addAll(users: SizedIterable<User>) {
        list.addAll(users)
    }

    override fun remove(user: User) {
        transaction {
            user.delete()
        }

        list.remove(user)
    }

    override fun users(): List<User> {
        return list;
    }
}