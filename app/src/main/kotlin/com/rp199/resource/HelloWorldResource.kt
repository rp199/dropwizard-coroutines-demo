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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletionStage
import kotlin.coroutines.cancellation.CancellationException

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class HelloWorldResource(
    private val asyncResponseScope: CoroutineScope,
    private val suspendingService: SuspendingService,
) {

    /**
     * Blocking approach: Wraps the call with [runBlocking], which blocks the HTTP thread until the work is done.
     * Not ideal in an async framework as it prevents the server from handling other requests concurrently.
     */
    @GET
    @Path("/blocking")
    fun getBlocking(): String {
        runBlocking {
            suspendingService.doWorkBlocking()
        }
        return "Hello from blocking!"
    }

    /**
     * Uses a dedicated [asyncResponseScope] to offload the work by converting the coroutine to a [CompletionStage] via the `future` extension.
     * This approach delegates most of the complexity to the framework and handles coroutine completion and cancellation automatically.
     */
    @GET
    @Path("/coroutines-using-future")
    fun getCoroutine(): CompletionStage<String> {
        return asyncResponseScope.future {
            suspendingService.doWork()
            "Hello from the coroutine using futures!"
        }
    }

    /**
     * Uses a dedicated [asyncResponseScope] to launch a coroutine per request and manually resume the [AsyncResponse] when the work is done.
     * Requires explicit handling of errors, timeouts, and coroutine cancellation if the client disconnects or the request times out.
     * Useful when fine-grain control over request lifecycle and error handling is needed.
     */
    @GET
    @Path("/coroutine-using-async-response")
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
}
