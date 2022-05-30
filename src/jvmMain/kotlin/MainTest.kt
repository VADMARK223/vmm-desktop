import kotlinx.coroutines.*

/**
 * @author Markitanov Vadim
 * @since 29.05.2022
 */
fun main1() = runBlocking {
    println("Start: ${Thread.currentThread().name}")
    launch {
        loadFile()
        println("Load complete: ${Thread.currentThread().name}")
    }

    println("End: ${Thread.currentThread().name}")
}

suspend fun loadFile() {
    delay(3000L)
}

