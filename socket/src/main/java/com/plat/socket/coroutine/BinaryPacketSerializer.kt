package com.plat.socket.coroutine

import com.plat.socket.remote.Message
import com.plat.socket.serialize.Reader
import com.plat.socket.serialize.Serializer
import com.plat.socket.serialize.Writer
import com.plat.socket.serialize.binary.readBoolean
import com.plat.socket.serialize.binary.readInt
import com.plat.socket.serialize.binary.writeBoolean
import com.plat.socket.serialize.binary.writeInt

/**
 * Returns a binary [Serializer] for [Packet]?.
 * [messageSerializer] must be able to serialize [Message].
 */
public fun binaryPacketSerializer(messageSerializer: Serializer): Serializer = object : Serializer {
    override fun write(writer: Writer, value: Any?) = when (value) {
        null -> writer.writeBoolean(false)
        is Packet -> {
            writer.writeBoolean(true)
            writer.writeInt(value.requestNumber)
            messageSerializer.write(writer, value.message)
        }
        else -> error("unexpected value '$value'")
    }

    override fun read(reader: Reader): Packet? =
        if (reader.readBoolean()) Packet(reader.readInt(), messageSerializer.read(reader) as Message) else null
}