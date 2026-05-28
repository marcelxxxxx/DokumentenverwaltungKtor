package online.marcel.file

import io.ktor.http.ContentDisposition
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.MultiPartData
import io.ktor.server.application.Application
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.header
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.io.File

fun Application.fileModule() {
    val filemanager = FileManager()

    routing {
        get("/download") {
            val filename: String = call.parameters["filename"]!!
            val file = File("uploads/$filename")
            call.response.header(
                HttpHeaders.ContentDisposition,
                ContentDisposition.Attachment.withParameter(ContentDisposition.Parameters.FileName, filename)
                    .toString()
            )
            call.respondFile(file)
        }
        get("/filelist") {
            val email: String = call.request.queryParameters["email"]!!
            val resultFilelist: Result<MutableList<FileFromDB>> = filemanager.getFileList(email)

            if (resultFilelist.isSuccess) {
                call.respond(resultFilelist.getOrThrow())
            } else {
                call.respond(HttpStatusCode.InternalServerError)
            }
        }
        post("/uploadFile") {
            val email: String = call.queryParameters["email"]!!

            val multipartData: MultiPartData = call.receiveMultipart(formFieldLimit = 1024 * 1024 * 100) //100 MIB können hochgeladen werden
            filemanager.handleFileMultipartData(multipartData = multipartData, email = email)
            call.respondText("File is uploaded")
        }
    }
}