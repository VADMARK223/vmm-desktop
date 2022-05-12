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
import service.requestDefaultUser

class UsersRepoImpl : UsersRepo {
    private val current = mutableStateOf<User?>(null)
    private val users = mutableStateListOf<User>()

    private val userLoadListener = mutableStateListOf<() -> Unit>()

    init {
        if (requestDefaultUser()) {
            requestDefaultCurrentUser(1L)
        }
    }

    private fun requestDefaultCurrentUser(id: Long) {
        println("Request default current user: $id.")
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users/$id")
            if (response.status == HttpStatusCode.OK) {
                val userResponse = response.body<User>()
                println("Set current user: $userResponse")
                setCurrentUser(userResponse)
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

    override fun addListener(listener: () -> Unit) {
        userLoadListener.add(listener)
    }

    override fun setCurrentUser(user: User) {
        println("Set current user: $user")

        current.value = user
        userLoadListener.forEach {
            it.invoke()
        }
    }
}
