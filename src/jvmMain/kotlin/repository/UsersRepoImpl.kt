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

    private val userLoadListener = mutableStateListOf<(Long) -> Unit>()

    init {
        if (requestDefaultUser()) {
            requestDefaultCurrentUser(1L)
        }
        requestAll()
    }

    @Suppress("SameParameterValue")
    private fun requestDefaultCurrentUser(id: Long) {
        println("Request default current user: $id.")
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users/$id")
            if (response.status == HttpStatusCode.OK) {
                val userResponse = response.body<User>()
                setCurrentUser(userResponse)
            }
        }
    }

    override fun current(): MutableState<User?> {
        return current
    }

    override fun requestAll(): List<User> {
        users.clear()
        HttpService.coroutineScope.launch {
            val response = HttpService.client.get("${HttpService.host}/users")
            if (response.status == HttpStatusCode.OK) {
                users.addAll(response.body<List<User>>())
            }
        }

        return users
    }

    override fun all(): List<User> = users

    override fun addListener(listener: (userId: Long) -> Unit) {
        userLoadListener.add(listener)
    }

    override fun setCurrentUser(user: User) {
        println("Set current user: $user")

        current.value = user
        userLoadListener.forEach {
            it.invoke(user.id)
        }
    }

    override fun update(entity: User?) {
        for (user in users) {
            if (user.id == entity?.id) {
                users[users.indexOf(user)] = entity
                break
            }
        }
    }

    override fun getById(id: Long?): User? = users.singleOrNull { id == it.id }
}
