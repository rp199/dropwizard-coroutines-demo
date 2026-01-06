package com.rp199.resource

import com.rp199.service.SuspendingService
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.container.AsyncResponse
import jakarta.ws.rs.container.Suspended
import jakarta.ws.rs.core.MediaType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CompletionStage
import java.util.concurrent.Executors
import kotlin.coroutines.cancellation.CancellationException

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class HelloWorldResource(
    private val asyncResponseScope: CoroutineScope,
    private val suspendingService: SuspendingService,
) {
    private val executor = Executors.newCachedThreadPool()

    @GET
    @Path("/blocking")
    fun getBlocking(): String {
        runBlocking {
            suspendingService.doWorkBlocking()
        }
        return "Hello from blocking!"
    }

    @GET
    @Path("/async")
    fun getAsync(
        @Suspended asyncResponse: AsyncResponse
    ) {
        executor.submit {
            suspendingService.doWorkBlocking()
            asyncResponse.resume("Hello from the async!")
        }
    }

    @GET
    @Path("/coroutine-async-response")
    fun getCoroutine(@Suspended async: AsyncResponse) {
        val job = asyncResponseScope.launch {
            try {
                suspendingService.doWork()
                async.resume("Hello from the coroutine using async response!")
            } catch (_: CancellationException) {
                async.resume("Coroutine Cancelled!")
            } catch (e: Throwable) {
                async.resume(e)
            }
        }

        // Cancel coroutine if client disconnects
        async.register { _: Throwable? -> job.cancel() }

        // Cancel coroutine on timeout and resume HTTP
        async.setTimeoutHandler {
            job.cancel()
            async.resume(WebApplicationException(503))
        }
    }

    @GET
    @Path("/coroutine-future")
    fun getCoroutine(): CompletionStage<String> {
        return asyncResponseScope.future {
            suspendingService.doWork()
            "Hello from the coroutine using futures!"
        }
    }
}
