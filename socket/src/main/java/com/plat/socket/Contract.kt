package com.plat.socket

import com.plat.socket.coroutine.MustBeImplementedByAcceptor
import com.plat.socket.coroutine.MustBeImplementedByInitiator
import com.plat.socket.remote.ServiceId
import com.plat.socket.serialize.binary.BaseEncoder
import com.plat.socket.serialize.binary.IntEncoder
import com.plat.socket.serialize.binary.StringEncoder
import com.plat.socket.serialize.binary.enumEncoder
import com.plat.socket.serialize.binary.readLong
import com.plat.socket.serialize.binary.writeLong

// This file describes the contract (data transfer objects and interfaces) between client and server.

/**
 * The base types Boolean, Byte, Int, Long, Double, String and ByteArray are supported.
 * Other own base types like [MyDate] could be added.
 * In contrast to regular classes, own base types could implement a more efficient serializing.
 */
public class MyDate(public val currentTimeMillis: Long)

// Shows how to implement an own base type encoder.
private val MyDateEncoder = BaseEncoder(
    MyDate::class,
    { writer, value -> writer.writeLong(value.currentTimeMillis) },
    { reader -> MyDate(reader.readLong()) }
)

private fun Appendable.append(value: MyDate) {
    append("MyDate(${value.currentTimeMillis})")
}

/**
 * A concrete class must have a primary constructor and all its parameters must be properties.
 * Body properties are allowed but must be of `var` kind.
 * Properties can be optional.
 */
public class Address(
    public val street: String,
) {
    public var number: Int? = null
}

/** Enumerations are supported. */
public enum class Gender {
    Female,
    Male,
}

/** Lists are supported. */
public class Person(
    public val name: String,
    public val gender: Gender,
    public val birthday: MyDate,
    public val addresses: List<Address>,
)

/**
 * Exceptions are supported.
 * They are basically like regular classes but [Throwable.message] and [Throwable.cause] aren't serialized.
 */
public class DivideByZeroException : RuntimeException()

/**
 * Inheritance is supported.
 * Base class properties must be in body and abstract and overridden in subclasses.
 */
public abstract class BaseClass {
    public abstract val baseClassProperty: String
}

public class SubClass(
    public override val baseClassProperty: String,
    public val subClassProperty: String,
) : BaseClass()

/**
 * All functions must be suspendable because they need IO.
 * Overloading is not allowed.
 */
public interface Calculator {
    public suspend fun add(a: Int, b: Int): Int
    public suspend fun divide(a: Int, b: Int): Int
}

public interface NewsListener {
    public suspend fun notify(news: String)
}

@MustBeImplementedByAcceptor
public val CalculatorId: ServiceId<Calculator> = ServiceId(1)

@MustBeImplementedByInitiator
public val NewsListenerId: ServiceId<NewsListener> = ServiceId(2)

/** Define all the base encoders needed by the contract (including enumerations and own base types). */
internal val BaseEncoders = listOf(
    IntEncoder,
    StringEncoder,
    MyDateEncoder,
    enumEncoder<Gender>(),
)

internal val TreeConcreteClasses = listOf(
    Address::class,
    Person::class,
    DivideByZeroException::class,
    SubClass::class,
)

internal val Services = listOf(
    Calculator::class,
    NewsListener::class,
)

internal fun Appendable.dumpValue(value: Any) {
    // Writes value (without line breaks) if responsible else does nothing.
    when (value) {
        is MyDate -> append(value)
    }
}
