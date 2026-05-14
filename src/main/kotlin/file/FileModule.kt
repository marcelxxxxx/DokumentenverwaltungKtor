package online.marcel.file

import io.ktor.http.content.MultiPartData
import io.ktor.server.application.Application
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File

fun Application.fileModule() {
    val filemanager = FileManager()

    routing {
        post("/uploadFile") {
            var fileName = ""
            val multipartData: MultiPartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100) //100 MIB können hochgeladen werden

            filemanager.handleFileMultipartData(multipartData = multipartData)

            call.respondText("$fileName is uploaded to 'uploads/$fileName'")
        }
    }
}