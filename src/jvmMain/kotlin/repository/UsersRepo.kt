package repository

import androidx.compose.runtime.mutableStateListOf
import db.User
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import kotlin.random.Random

object UsersRepo {
    private var list = mutableStateListOf<User>()

    init {
        transaction {
            addLogger(StdOutSqlLogger)
            list.addAll(User.all())
        }
    }

    fun users(): List<User> {
        return list
    }

    fun addUser() {
        transaction {
            val user = User.new {
                firstName = "FirstName#" + Random.nextInt(1000)
                lastName = "LastName#" + Random.nextInt(1000)
                activityTime = Instant.now()
            }
            list.add(user)
        }
    }

    fun getFirst(): User? {
        return if (!list.isEmpty()) list.first() else null
    }

    fun remove(user: User) {
        transaction {
            user.delete()
        }

        list.remove(user)
    }
}

