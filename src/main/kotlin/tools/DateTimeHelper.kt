package online.marcel.tools

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime


object DateTimeHelper {

    fun getCurrentDateTime(): kotlinx.datetime.LocalDateTime {
        return LocalDateTime.now().toKotlinLocalDateTime()
    }

    fun plusDays(days: Int, localdate: LocalDate): LocalDate {
        return localdate.plus(days, DateTimeUnit.DAY)
    }

    fun convertDateStringToLocalDateTime(dateString: String): kotlinx.datetime.LocalDateTime {
        return LocalDateTime.parse(dateString).toKotlinLocalDateTime()
    }

    fun convertDateStringToLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString)
    }

}