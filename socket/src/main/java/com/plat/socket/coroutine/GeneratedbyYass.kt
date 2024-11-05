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

package com.plat.socket.coroutine

public fun <F, I> com.plat.socket.coroutine.FlowService<F, I>.proxy(
    suspendIntercept: com.plat.socket.SuspendInterceptor,
): com.plat.socket.coroutine.FlowService<F, I> = object : com.plat.socket.coroutine.FlowService<F, I> {
    override suspend fun cancel(
        p1: kotlin.Int,
    ) {
        suspendIntercept(com.plat.socket.coroutine.FlowService<F, I>::cancel, listOf(p1)) {
            this@proxy.cancel(p1)
        }
    }

    override suspend fun create(
        p1: I,
    ): kotlin.Int {
        return suspendIntercept(com.plat.socket.coroutine.FlowService<F, I>::create, listOf(p1)) {
            this@proxy.create(p1)
        } as kotlin.Int
    }

    override suspend fun next(
        p1: kotlin.Int,
    ): F? {
        return suspendIntercept(com.plat.socket.coroutine.FlowService<F, I>::next, listOf(p1)) {
            this@proxy.next(p1)
        } as F?
    }
}

public fun <F, I> com.plat.socket.remote.ServiceId<com.plat.socket.coroutine.FlowService<F, I>>.proxy(
    tunnel: com.plat.socket.remote.Tunnel,
): com.plat.socket.coroutine.FlowService<F, I> =
    object :com.plat.socket.coroutine.FlowService<F, I> {
        override suspend fun cancel(
            p1: kotlin.Int,
        ) {
            tunnel(com.plat.socket.remote.Request(id, 0, listOf(p1)))
                .process()
        }

        override suspend fun create(
            p1: I,
        ) =
            tunnel(com.plat.socket.remote.Request(id, 1, listOf(p1)))
                .process() as kotlin.Int

        override suspend fun next(
            p1: kotlin.Int,
        ) =
            tunnel(com.plat.socket.remote.Request(id, 2, listOf(p1)))
                .process() as F?
    }

public fun <F, I> com.plat.socket.remote.ServiceId<com.plat.socket.coroutine.FlowService<F, I>>.service(
    implementation: com.plat.socket.coroutine.FlowService<F, I>,
): com.plat.socket.remote.Service =
    com.plat.socket.remote.Service(id) { functionId, parameters ->
        when (functionId) {
            0 -> implementation.cancel(
                parameters[0] as kotlin.Int,
            )
            1 -> implementation.create(
                parameters[0] as I,
            )
            2 -> implementation.next(
                parameters[0] as kotlin.Int,
            )
            else -> error("service with id $id has no function with id $functionId")
        }
    }