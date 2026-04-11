package online.marcel.register

import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.register() {
    val registerManager = RegisterManager()
    routing {
        post("/register") {
            val register: Register = call.receive<Register>()
        }
    }
}