package com.plat.socket.ktor

import com.plat.socket.remote.Message
import com.plat.socket.remote.Reply
import com.plat.socket.remote.Tunnel
import com.plat.socket.transport.Transport
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.content.OutgoingContent
import io.ktor.http.contentLength
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.writeFully

internal fun Transport.write(message: Message): OutgoingContent.WriteChannelContent {
    val writer = createWriter()
    write(writer, message)
    return object : OutgoingContent.WriteChannelContent() {
        override suspend fun writeTo(channel: ByteWriteChannel) = channel.writeFully(writer.buffer, 0, writer.current)
        override val contentLength get() = writer.current.toLong()
    }
}

fun HttpClient.tunnel(
    transport: Transport,
    url: String,
    headers: () -> Headers = { Headers.Empty },
): Tunnel = { request ->
    val response = request(url) {
        method = HttpMethod.Post
        this.headers.appendAll(headers())
        setBody(transport.write(request))
    }
    val length = response.contentLength()!!.toInt()
    response.bodyAsChannel().read(transport, length) as Reply
}