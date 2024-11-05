package com.plat.socket.remote

@Suppress("unused")
class ServiceId<S : Any>(val id: Int)

class Service(internal val id: Int, internal val tunnel: suspend (functionId: Int, parameters: List<Any?>) -> Any?)

typealias Tunnel = suspend (request: Request) -> Reply

fun tunnel(vararg services: Service): Tunnel {
    val id2service = services.associateBy(Service::id)
    require(id2service.size == services.size) { "duplicated service id" }
    return { request ->
        val service = id2service[request.serviceId] ?: error("no service with id ${request.serviceId}")
        try {
            val result = service.tunnel(request.functionId, request.parameters)
            ValueReply(if (result === Unit) null else result)
        } catch (e: Exception) {
            ExceptionReply(e)
        }
    }
}