package online.marcel.file

import online.marcel.db.DBManager
import online.marcel.file.dataclass.FileFromDB
import online.marcel.file.dataclass.UploadFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class FilePersistence {

    fun saveUploadFile(uploadFiles: MutableList<UploadFile>) {
        val stm = "INSERT INTO file (originalFilename, newFilename, pathToFile, uploadFileBy) VALUES (?, ?, ?, ?)"

        DBManager.getConnection().use { conn: Connection ->
            conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
                for (upload: UploadFile in uploadFiles) {
                    pStmt.setString(1, upload.originalFilename)
                    pStmt.setString(2, upload.newFilename)
                    pStmt.setString(3, upload.pathToFile)
                    pStmt.setInt(4, upload.uploadFrom)
                    pStmt.addBatch()
                }
                pStmt.executeBatch()
            }
        }
    }

    fun getFileList(userid: Int): MutableList<FileFromDB> {
        val stm = "SELECT id, originalFilename, newFilename FROM file WHERE uploadFileBy = ? AND deleted IS NULL"
        val fileList = mutableListOf<FileFromDB>()

        DBManager.getConnection().use { conn: Connection ->
            conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
                pStmt.setInt(1, userid)
                pStmt.executeQuery().use { rs: ResultSet ->
                    while (rs.next()) {
                        val path: Path =  File(rs.getString(3)).toPath()
                        val mimeType: String = Files.probeContentType(path)

                        val filefromdb: FileFromDB = FileFromDB(
                            id = rs.getInt(1),
                            originalFilename = rs.getString(2),
                            newFilename = rs.getString(3),
                            filetype = mimeType
                        )
                        fileList.add(filefromdb)
                    }
                }
            }
        }

        return fileList
    }

    fun getDatasetForEmailNotificationById()

}