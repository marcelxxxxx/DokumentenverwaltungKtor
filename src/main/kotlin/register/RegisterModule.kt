package online.marcel.register

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.register() {
    val registerManager = RegisterManager()
    routing {
        post("/register") {
            val register: Register = call.receive<Register>()

            println(register)

            val result: Result<Boolean> = registerManager.registerNewUser(register)
            if (result.isSuccess && result.getOrNull() == true) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}