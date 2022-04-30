package repository

import androidx.compose.runtime.mutableStateListOf
import db.MessageNew
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import service.HttpService
import kotlin.random.Random

/**
 * @author Markitanov Vadim
 * @since 30.04.2022
 */
class MessagesRepoNewImpl : MessagesRepoNew {
    private val messages = mutableStateListOf<MessageNew>()

    init {
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
        HttpService.coroutineScope.launch {
            HttpService.client.delete("${HttpService.host}/messages/${message.id}")
            messages.remove(message)
        }
        messages.remove(message)
    }

    override fun messagesByConversationId(id: Long) {
        messages.clear()
        HttpService.coroutineScope.launch {
            val responseMessages =
                HttpService.client.get("${HttpService.host}/messages/conversation/${id}").call.body<List<MessageNew>>()
            messages.addAll(responseMessages)
        }
    }

    override fun addMessage(text: String) {
        println("Add message: $text")
        messages.add(MessageNew(Random.nextLong(), text = text))
    }
}