import kotlinx.coroutines.*

/**
 * @author Markitanov Vadim
 * @since 29.05.2022
 */
@OptIn(DelicateCoroutinesApi::class)
fun main1() = runBlocking {
    println("Start: ${Thread.currentThread().name}")
    val job = GlobalScope.launch {
        loadFile()
        println("Load complete: ${Thread.currentThread().name}")
    }

    println("End: ${Thread.currentThread().name}")
    job.join()
}

suspend fun loadFile() {
    delay(3000L)
}

