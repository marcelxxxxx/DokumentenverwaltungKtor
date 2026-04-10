package online.marcel

import io.ktor.server.application.*
import online.marcel.login.login

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    login()
}
