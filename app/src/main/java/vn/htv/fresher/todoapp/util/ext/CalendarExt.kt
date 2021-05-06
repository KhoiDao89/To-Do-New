package vn.htv.fresher.todoapp.util.ext

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.*

fun Calendar.toLocalDateTime(): LocalDateTime {
  val instant = DateTimeUtils.toInstant(this)
  return LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
}