@file:Suppress(
    "UNCHECKED_CAST",
    "USELESS_CAST",
    "PARAMETER_NAME_CHANGED_ON_OVERRIDE",
    "unused",
    "RemoveRedundantQualifierName",
    "SpellCheckingInspection",
    "RedundantVisibilityModifier",
    "RedundantNullableReturnType",
    "KotlinRedundantDiagnosticSuppress",
    "RedundantSuppression",
)

package com.plat.socket

public fun Calculator.proxy(
    suspendIntercept: com.plat.socket.SuspendInterceptor,
): Calculator = object : Calculator {
    override suspend fun add(
        p1: kotlin.Int,
        p2: kotlin.Int,
    ): kotlin.Int {
        return suspendIntercept(Calculator::add, listOf(p1, p2)) {
            this@proxy.add(p1, p2)
        } as kotlin.Int
    }

    override suspend fun divide(
        p1: kotlin.Int,
        p2: kotlin.Int,
    ): kotlin.Int {
        return suspendIntercept(Calculator::divide, listOf(p1, p2)) {
            this@proxy.divide(p1, p2)
        } as kotlin.Int
    }
}

public fun com.plat.socket.remote.ServiceId<Calculator>.proxy(
    tunnel: com.plat.socket.remote.Tunnel,
): Calculator =
    object : Calculator {
        override suspend fun add(
            p1: kotlin.Int,
            p2: kotlin.Int,
        ) =
            tunnel(com.plat.socket.remote.Request(id, 0, listOf(p1, p2)))
                .process() as kotlin.Int

        override suspend fun divide(
            p1: kotlin.Int,
            p2: kotlin.Int,
        ) =
            tunnel(com.plat.socket.remote.Request(id, 1, listOf(p1, p2)))
                .process() as kotlin.Int
    }

public fun com.plat.socket.remote.ServiceId<Calculator>.service(
    implementation: Calculator,
): com.plat.socket.remote.Service =
    com.plat.socket.remote.Service(id) { functionId, parameters ->
        when (functionId) {
            0 -> implementation.add(
                parameters[0] as kotlin.Int,
                parameters[1] as kotlin.Int,
            )
            1 -> implementation.divide(
                parameters[0] as kotlin.Int,
                parameters[1] as kotlin.Int,
            )
            else -> error("service with id $id has no function with id $functionId")
        }
    }

public fun NewsListener.proxy(
    suspendIntercept: com.plat.socket.SuspendInterceptor,
): NewsListener = object : NewsListener {
    override suspend fun notify(
        p1: kotlin.String,
    ) {
        suspendIntercept(NewsListener::notify, listOf(p1)) {
            this@proxy.notify(p1)
        }
    }
}

public fun com.plat.socket.remote.ServiceId<NewsListener>.proxy(
    tunnel: com.plat.socket.remote.Tunnel,
): NewsListener =
    object : NewsListener {
        override suspend fun notify(
            p1: kotlin.String,
        ) {
            tunnel(com.plat.socket.remote.Request(id, 0, listOf(p1)))
                .process()
        }
    }

public fun com.plat.socket.remote.ServiceId<NewsListener>.service(
    implementation: NewsListener,
): com.plat.socket.remote.Service =
    com.plat.socket.remote.Service(id) { functionId, parameters ->
        when (functionId) {
            0 -> implementation.notify(
                parameters[0] as kotlin.String,
            )
            else -> error("service with id $id has no function with id $functionId")
        }
    }

public fun createSerializer(
    baseEncoders: kotlin.collections.List<com.plat.socket.serialize.binary.BaseEncoder<out kotlin.Any>>,
): com.plat.socket.serialize.binary.BinarySerializer =
    com.plat.socket.serialize.binary.BinarySerializer(baseEncoders + listOf(
        com.plat.socket.serialize.binary.ClassEncoder(
            Address::class, false,
            { w, i ->
                w.writeNoIdRequired(4, i.street)
                w.writeNoIdOptional(3, i.number)
            },
            { r ->
                val i = Address(
                    r.readNoIdRequired(4) as String,
                )
                i.number = r.readNoIdOptional(3) as kotlin.Int?
                i
            }
        ),
        com.plat.socket.serialize.binary.ClassEncoder(
            Person::class, false,
            { w, i ->
                w.writeNoIdRequired(4, i.name)
                w.writeNoIdRequired(6, i.gender)
                w.writeNoIdRequired(5, i.birthday)
                w.writeNoIdRequired(1, i.addresses)
            },
            { r ->
                val i = Person(
                    r.readNoIdRequired(4) as String,
                    r.readNoIdRequired(6) as Gender,
                    r.readNoIdRequired(5) as MyDate,
                    r.readNoIdRequired(1) as List<Address>,
                )
                i
            }
        ),
        com.plat.socket.serialize.binary.ClassEncoder(
            DivideByZeroException::class, false,
            { _, _ -> },
            {
                val i = DivideByZeroException(
                )
                i
            }
        ),
        com.plat.socket.serialize.binary.ClassEncoder(
            SubClass::class, false,
            { w, i ->
                w.writeNoIdRequired(4, i.baseClassProperty)
                w.writeNoIdRequired(4, i.subClassProperty)
            },
            { r ->
                val i = SubClass(
                    r.readNoIdRequired(4) as String,
                    r.readNoIdRequired(4) as String,
                )
                i
            }
        ),
    ))

public fun createDumper(dumpValue: kotlin.text.Appendable.(value: kotlin.Any) -> kotlin.Unit): com.plat.socket.Dumper =
    com.plat.socket.createDumper(
        com.plat.socket.dumperProperties(
            Address::class to listOf(
                Address::number as kotlin.reflect.KProperty1<Any, Any?>,
                Address::street as kotlin.reflect.KProperty1<Any, Any?>,
            ),
            Person::class to listOf(
                Person::addresses as kotlin.reflect.KProperty1<Any, Any?>,
                Person::birthday as kotlin.reflect.KProperty1<Any, Any?>,
                Person::gender as kotlin.reflect.KProperty1<Any, Any?>,
                Person::name as kotlin.reflect.KProperty1<Any, Any?>,
            ),
            DivideByZeroException::class to listOf(
            ),
            SubClass::class to listOf(
                SubClass::baseClassProperty as kotlin.reflect.KProperty1<Any, Any?>,
                SubClass::subClassProperty as kotlin.reflect.KProperty1<Any, Any?>,
            ),
        ),
        setOf(
        ),
        dumpValue,
    )
