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
        current.value = User(1, "Vadim", "Markitanov")

        users.clear()
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users")
            if (response.status == HttpStatusCode.OK) {
                val usersResponse = response.body<List<User>>()
                users.addAll(usersResponse)
            }
        }
    }

    override fun current(): MutableState<User?> {
        return current
    }

    override fun all(): List<User> {
        return users
    }
}
