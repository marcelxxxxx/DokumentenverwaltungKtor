package online.marcel.login

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing

fun Application.login() {
    val loginManager = LoginManager()
    routing {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            val result: Result<Boolean> = loginManager.handleLogin(loginRequest)
            if (result.isSuccess && result.getOrNull() == true) {
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}