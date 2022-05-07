package repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import model.User

class UsersRepoImpl : UsersRepo {
    private val current = mutableStateOf<User?>(null)

    init {
        current.value = User(1, "Vadim", "Markitanov")
    }

    override fun current(): MutableState<User?> {
        return current
    }
}
