package io.grpc.healthcheck

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.StatusException
import io.grpc.healthcheck.HealthGrpcKt.HealthCoroutineStub
import java.io.Closeable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.runBlocking

class GetVersionClient(val channel: ManagedChannel) : Closeable {
    private val stub: HealthCoroutineStub = HealthCoroutineStub(channel)

    fun getVersionOfStorageSystem(s: String) = runBlocking {
        val request = versionRequest { version = s }
        try {
            val response = stub.getVersion(request)
            println("Greeter client received: ${response.message}")
        } catch (e: StatusException) {
            println("RPC failed: ${e.status}")
        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}

/**
 * Greeter, uses first argument as name to greet if present;
 * greets "world" otherwise.
 */
fun main(args: Array<String>) {
    val isRemote = args.size == 1

    Executors.newFixedThreadPool(10).asCoroutineDispatcher().use { dispatcher ->
        val builder = if (isRemote)
            ManagedChannelBuilder.forTarget(args[0].removePrefix("https://") + ":443").useTransportSecurity()
        else
            ManagedChannelBuilder.forTarget("localhost:50051").usePlaintext()

        GetVersionClient(
            builder.executor(dispatcher.asExecutor()).build()
        ).use {
            val dbVersion = args.singleOrNull() ?: "1"
            it.getVersionOfStorageSystem(dbVersion)
        }
    }
}
