package repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import model.User
import service.HttpService

class UsersRepoImpl : UsersRepo {
    private val current = mutableStateOf<User?>(null)
    private val users = mutableStateListOf<User>()

    init {
        requestDefaultCurrentUser(3L)
    }

    private fun requestDefaultCurrentUser(id: Long) {
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users/$id")
            if (response.status == HttpStatusCode.OK) {
                val usersResponseData = response.body<User>()
                current.value = usersResponseData
            }
        }
    }

    override fun current(): MutableState<User?> {
        return current
    }

    override fun all(): List<User> {
        users.clear()
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users")
            if (response.status == HttpStatusCode.OK) {
                val usersResponse = response.body<List<User>>()
                for (user in usersResponse) {
                    if (user.id != current.value?.id) {
                        users.add(user)
                    }
                }
            }
        }

        return users
    }
}
