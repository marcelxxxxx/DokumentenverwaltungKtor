package online.marcel.tools

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.sql.PreparedStatement
import java.sql.Types

object SQLHelper {

    fun setStringInPreparedStatement(pStmt: PreparedStatement, index: Int, text: String?) {
        if (text == null) {
            pStmt.setNull(index, Types.NULL)
        } else {
            pStmt.setString(index, text)
        }
    }

    fun setIntInPreparedStatement(pStmt: PreparedStatement, index: Int, number: Int?) {
        if (number == null) {
            pStmt.setNull(index, Types.NULL)
        } else {
            pStmt.setInt(index, number)
        }
    }

    fun setLocalDateInPreparedStatement(pStmt: PreparedStatement, index: Int, localDate: LocalDate?) {
        if (localDate == null) {
            pStmt.setNull(index, Types.NULL)
        } else {
            pStmt.setObject(index, localDate.toJavaLocalDate())
        }
    }

}