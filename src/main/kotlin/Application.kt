package online.marcel

import io.ktor.server.application.*
import online.marcel.file.fileModule
import online.marcel.login.login
import online.marcel.register.register

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    register()
    login()
    fileModule()
}
