package online.marcel.db

import java.sql.Connection
import java.sql.DriverManager

object DBManager {

    fun getConnection(): Connection {
        return DriverManager.getConnection("jdbc:mysql://${DBData.HOST}:${DBData.PORT}/${DBData.DB_NAME}?user=${DBData.USER}&password=${DBData.PASSWORD}")
    }

}