package com.plat.socket

import com.plat.socket.coroutine.binaryPacketSerializer
import  com.plat.socket.serialize.Serializer
import  com.plat.socket.transport.Transport
import com.plat.socket.transport.binaryMessageSerializer


// This file describes the contract that depends on generated artifacts.

public val ContractSerializer: Serializer = createSerializer(BaseEncoders)
public val MessageSerializer: Serializer = binaryMessageSerializer(ContractSerializer)
public val PacketSerializer: Serializer = binaryPacketSerializer(MessageSerializer)

private const val INITIAL_WRITER_CAPACITY = 100
public val MessageTransport: Transport = Transport(MessageSerializer, INITIAL_WRITER_CAPACITY)
public val PacketTransport: Transport = Transport(PacketSerializer, INITIAL_WRITER_CAPACITY)

public val dumper: Dumper = createDumper(Appendable::dumpValue)
