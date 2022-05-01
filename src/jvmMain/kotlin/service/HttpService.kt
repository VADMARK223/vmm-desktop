package service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json

object HttpService {
    const val host: String = "http://localhost:8888"

    lateinit var coroutineScope: CoroutineScope

    val client = HttpClient(CIO) {
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.HEADERS
//        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            })
        }
    }
}