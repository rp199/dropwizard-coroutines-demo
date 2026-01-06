package com.rp199.resource

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import java.util.UUID

@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class HelloWorldResource {

    @POST
    fun getHelloWorld(request: Request): Response {
        return Response(
            inputMessage = request.message,
            outputMessage = "Hello. Here's a random UUID: ${UUID.randomUUID()}"
        )
    }

    data class Request(val message: String)
    data class Response(val inputMessage: String, val outputMessage: String)
}
