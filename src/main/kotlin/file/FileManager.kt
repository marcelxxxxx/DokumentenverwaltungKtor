package online.marcel.file

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import online.marcel.core.ApplicationManager
import online.marcel.core.ApplicationUser
import online.marcel.file.dataclass.FileFromDB
import online.marcel.file.dataclass.UpdateDateRequest
import online.marcel.file.dataclass.UploadFile
import java.nio.file.Path
import java.io.File
import java.util.UUID

class FileManager {

    private val filePersistence = FilePersistence()

    private fun createUploadDir() {
        val uploadFileDir: File = File("uploads")

        if (!uploadFileDir.exists()) {
            uploadFileDir.mkdirs()
        }
    }

    suspend fun handleFileMultipartData(multipartData: MultiPartData, email: String) {
        this.createUploadDir()

        val result: Result<ApplicationUser> = ApplicationManager.getApplicationUser(email)
        val applicationUser: ApplicationUser = result.getOrThrow()

        val fileuploadlist: MutableList<UploadFile> = mutableListOf()

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FileItem -> {
                    var originalFileName: String = ""
                    var newFilename: String = UUID.randomUUID().toString()
                    val fileName: String? = part.originalFileName

                    originalFileName = if (fileName != null) {
                        Path.of(fileName).fileName.toString()
                    } else {
                        newFilename
                    }

                    val fileExtension: File = File(originalFileName)
                    newFilename += ".${fileExtension.extension}"

                    fileuploadlist.addLast(
                        UploadFile(
                            originalFilename = originalFileName,
                            newFilename = newFilename,
                            pathToFile = "uploads/${newFilename}",
                            uploadFrom = applicationUser.id
                        )
                    )

                    val file: File = File("uploads/$newFilename")
                    part.provider().copyAndClose(file.writeChannel())
                }
                else -> {}
            }
            part.dispose()
        }

        this.filePersistence.saveUploadFile(fileuploadlist)
    }

    fun getFileList(email: String): Result<MutableList<FileFromDB>> {
        try {
            val resultApplicationUser: Result<ApplicationUser> = ApplicationManager.getApplicationUser(email)
            val applicationUser: ApplicationUser = resultApplicationUser.getOrThrow()
            return Result.success(this.filePersistence.getFileList(applicationUser.id))
        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }
    }

    fun updateDateForEmailnotification(updateDateRequest: UpdateDateRequest, email: String): Result<Boolean> {
        try {
            val resultApplication: Result<ApplicationUser> = ApplicationManager.getApplicationUser(email)
            val applicationUser: ApplicationUser = resultApplication.getOrThrow()


        } catch (e: Exception) {
            e.printStackTrace()
            return Result.failure(e)
        }

        return Result.failure(Exception("Update"))
    }

}