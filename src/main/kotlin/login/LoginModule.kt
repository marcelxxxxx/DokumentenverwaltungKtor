package online.marcel.login

import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.login() {
    val loginManager = LoginManager()
    routing {
        post("/login") {
            val login = call.receive<Login>()


        }
    }
}