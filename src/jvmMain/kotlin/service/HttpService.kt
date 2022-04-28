package service

import androidx.compose.runtime.mutableStateOf
import db.TempUser
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object HttpService {
    private const val host: String = "http://localhost:8888"

    val selectedUser = mutableStateOf<TempUser?>(null)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    fun selectUser(coroutineScope: CoroutineScope, userId: Long) {
        coroutineScope.launch {
            val tempUser: TempUser = client.get("$host/user/${userId}").body()
            println("New selected user: $tempUser")
            selectedUser.value = tempUser
        }
    }
}