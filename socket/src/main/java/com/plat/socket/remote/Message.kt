package com.plat.socket.remote

sealed interface Message

class Request(val serviceId: Int, val functionId: Int, val parameters: List<Any?>) : Message

sealed interface Reply : Message {
    fun process(): Any?
}

class ValueReply(val value: Any?) : Reply {
    override fun process(): Any? = value
}

class ExceptionReply(val exception: Exception) : Reply {
    override fun process(): Nothing = throw exception
}