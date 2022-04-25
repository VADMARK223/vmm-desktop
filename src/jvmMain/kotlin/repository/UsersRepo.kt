package repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import db.User
import db.Users
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import kotlin.random.Random

object UsersRepo {
    val selected = mutableStateOf<User?>(null)
    private var list = mutableStateListOf<User>()

    init {
        transaction {
            addLogger(StdOutSqlLogger)
            list.addAll(User.all().orderBy(Users.id to SortOrder.ASC))
            selectFirst()
        }
    }

    fun users(): List<User> {
        return list
    }

    fun addUser(
        firstNameOut: String = "First#" + Random.nextInt(10),
        lastNameOut: String = "Last#" + Random.nextInt(10)
    ) {
        transaction {
            val user = User.new {
                firstName = firstNameOut
                lastName = lastNameOut
                activityTime = Instant.now()
            }
            list.add(user)
            selected.value = user
        }
    }

    fun remove(user: User) {
        transaction {
            user.delete()
        }

        list.remove(user)
        selectFirst()
    }

    private fun selectFirst() {
        selected.value = if (!list.isEmpty()) list.first() else null
    }
}

