package common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import service.HttpService
import service.requestDefaultUserId

/**
 * @author Markitanov Vadim
 * @since 23.05.2022
 */
object UsersRepo {
    private val current = mutableStateOf<User?>(null)
    private val users = mutableStateListOf<User>()

    init {
        val defaultUserId = requestDefaultUserId()

        if (defaultUserId != null) {
            requestDefaultCurrentUser(defaultUserId)
        }
        requestAll()
    }

    private fun requestDefaultCurrentUser(id: Long) {
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users/$id")
            if (response.status == HttpStatusCode.OK) {
                val userResponse = response.body<User>()
                setCurrentUser(userResponse)
            }
        }
    }

    fun current(): MutableState<User?> {
        return current
    }

    fun requestAll(withoutCurrentUser: Boolean = false): List<User> {
        users.clear()
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users")
            if (response.status == HttpStatusCode.OK) {
                val responseUsers = response.body<List<User>>()
                if (withoutCurrentUser) {
                    for (user in responseUsers) {
                        if (user.id != current.value?.id) {
                            users.add(user)
                        }
                    }
                } else {
                    users.addAll(responseUsers)
                }
            }
        }

        return users
    }

    fun all(): List<User> = users

    fun setCurrentUser(user: User) {
        current.value = user
    }

    fun update(entity: User) {
        /*for (user in users) {
            if (user.id == entity?.id) {
                users[users.indexOf(user)] = entity
                break
            }
        }*/

        HttpService.coroutineScope.launch {
            HttpService.client.post("${HttpService.host}/users") {
                contentType(ContentType.Application.Json)
                setBody(UserDto(entity.id, entity.firstName, entity.lastName))
            }
        }
    }

    fun getById(id: Long?): User? = users.singleOrNull { id == it.id }
}

@Serializable
data class User(
    val id: Long,
    var firstName: String,
    var lastName: String = "",
    val createTime: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val online: Boolean = false,
    val image: ByteArray? = null
) {
    val name: String
        get() = "$firstName $lastName"

    override fun toString(): String {
        return "Id: " + this.id.toString()
    }
}

@Serializable
data class UserDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val image: ByteArray? = null
)