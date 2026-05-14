package online.marcel.file

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File

class FileManager {

    suspend fun handleFileMultipartData(multipartData: MultiPartData) {
        val uploadFileDir = File("uploads")

        if (!uploadFileDir.exists()) {
            uploadFileDir.mkdirs()
        }

        var fileName = ""

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    fileName = part.originalFileName as String
                    val file = File("uploads/$fileName")
                    part.provider().copyAndClose(file.writeChannel())
                }
                else -> {}
            }
            part.dispose()
        }
    }

}