package vn.htv.fresher.todoapp.data.db.converter

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class DateTimeConverter {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        if (value == null) return null
        return LocalDateTime.parse(value, formatter)
    }

    @TypeConverter
    fun toString(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }
}