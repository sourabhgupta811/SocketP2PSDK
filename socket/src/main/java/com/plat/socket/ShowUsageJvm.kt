@file:Suppress("HttpUrlsUsage")

package com.plat.socket

import com.plat.socket.remote.tunnel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.ws
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.EmbeddedServer
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticFiles
import io.ktor.server.routing.routing
import io.ktor.server.websocket.webSocket
import java.io.File
import com.plat.socket.ktor.receiveLoop
import com.plat.socket.ktor.route
import com.plat.socket.ktor.tunnel

const val LOCAL_HOST: String = "localhost"
const val PORT: Int = 28947
private const val PATH = "/yass"

private fun Application.theModule() {
    install(io.ktor.server.websocket.WebSockets)
    routing {
        staticFiles("/", File("./")) // needed for debugging (sources)
        staticFiles("/", File("./build/js/packages/tutorial/kotlin"))

        // shows server-side unidirectional remoting with Http
        route(MessageTransport, PATH, tunnel(
            CalculatorId.service(CalculatorImpl),
        ))

        // shows server-side session based bidirectional remoting with WebSocket
        webSocket(PATH) { receiveLoop(PacketTransport, acceptorSessionFactory()) }
    }
}

fun createKtorServer(): EmbeddedServer<*, *> = embeddedServer(io.ktor.server.cio.CIO, PORT, module = Application::theModule)

private suspend fun useKtorRemoting() {
    println("*** useKtorRemoting ***")
    val server = createKtorServer()
    server.start()
    try {
        HttpClient(io.ktor.client.engine.cio.CIO) {
            install(io.ktor.client.plugins.websocket.WebSockets)
        }.use { client ->
            // shows client-side unidirectional remoting with Http
            useServices(client.tunnel(MessageTransport, "http://$LOCAL_HOST:$PORT$PATH"))

            // shows client-side session based bidirectional remoting with WebSocket
            client.ws("ws://$LOCAL_HOST:$PORT$PATH") { receiveLoop(PacketTransport, initiatorSessionFactory()) }
        }
    } finally {
        server.stop()
    }
}

suspend fun startServer() {
    showUsage()
    useKtorRemoting()
}