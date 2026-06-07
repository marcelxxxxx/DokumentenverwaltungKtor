package online.marcel.file

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import online.marcel.db.DBManager
import online.marcel.file.dataclass.EmailNotificationRowFromDB
import online.marcel.file.dataclass.FileFromDB
import online.marcel.file.dataclass.UploadFile
import online.marcel.tools.SQLHelper
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
        val stm = "SELECT file.id, originalFilename, newFilename, en.date, en.sendnotification FROM file " +
                "LEFT JOIN emailnotification AS en ON (en.fileid = file.id AND en.deleted IS NULL) " +
                "WHERE uploadFileBy = ? AND file.deleted IS NULL"
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
                            filetype = mimeType,
                            date = SQLHelper.getLocalDate(rs, 4),
                            sendnotification = SQLHelper.getLocalDateTime(rs, 5)
                        )
                        fileList.add(filefromdb)
                    }
                }
            }
        }

        return fileList
    }

    fun getDatasetForEmailNotificationByFileid(conn: Connection, fileid: Int, userid: Int): List<EmailNotificationRowFromDB> {
        val emailnotificationlist: MutableList<EmailNotificationRowFromDB> = mutableListOf()

        val stm: String = "SELECT eb.id, eb.fileid, eb.date FROM emailnotification AS eb " +
                "JOIN file ON (file.id = eb.fileid) " +
                "WHERE file.deleted IS NULL AND eb.deleted IS NULL AND eb.fileid = ? AND file.uploadFileBy = ?;"

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setInt(1, fileid)
            pStmt.setInt(2, userid)
            pStmt.executeQuery().use { rs: ResultSet ->
                while (rs.next()) {
                    val id: Int = rs.getInt(1)
                    val fileid: Int = rs.getInt(2)
                    val date: LocalDate? = SQLHelper.getLocalDate(rs, 3)
                    emailnotificationlist.add(EmailNotificationRowFromDB(id = id, fileid = fileid, date = date!!))
                }
            }
        }

        return emailnotificationlist
    }

    fun insertDatasetInEmailNotification(conn: Connection, fileid: Int, date: LocalDate) {
        val stm: String = "INSERT INTO emailnotification (fileid, date) VALUES (?, ?);"

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setInt(1, fileid)
            pStmt.setObject(2, date.toJavaLocalDate())
            pStmt.execute()
        }
    }

    fun updateDatasetInEmailNotification(conn: Connection, rowid: Int, date: LocalDate) {
        val stm = "UPDATE emailnotification SET date = ?, sendnotification = ? WHERE id = ?;"

        conn.prepareStatement(stm).use { pStmt: PreparedStatement ->
            pStmt.setObject(1, date.toJavaLocalDate())
            pStmt.setNull(2, java.sql.Types.DATE)
            pStmt.setInt(3, rowid)
            pStmt.executeUpdate()
        }
    }
}