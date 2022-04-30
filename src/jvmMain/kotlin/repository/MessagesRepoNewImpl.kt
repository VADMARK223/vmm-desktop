package repository

import androidx.compose.runtime.mutableStateListOf
import db.MessageNew
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import service.HttpService

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
class MessagesRepoNewImpl : MessagesRepoNew {
    private val messages = mutableStateListOf<MessageNew>()

    init {
        println("Messages repo init.")

        HttpService.coroutineScope.launch {
            messages.clear()
            val responseMessages =
                HttpService.client.get("${HttpService.host}/messages/conversation/2").call.body<List<MessageNew>>()
            println("Response messages size: " + responseMessages.size)
            messages.addAll(responseMessages)
        }
    }

    override fun all(): List<MessageNew> {
        return messages
    }

    override fun delete(message: MessageNew) {
        messages.remove(message)
    }
}