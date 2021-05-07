package vn.htv.fresher.todoapp.util.ext

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime


val LocalDateTime.timeString: String
  get() {
    return "${hour}:${minute}"
  }

val LocalDateTime.dayString: String
  get() {
    val duration: Duration = Duration.between(this, LocalDateTime.now())

    val seconds = duration.seconds
    val minutes = duration.toMinutes()
    val hours   = duration.toHours()

    return if (seconds <= 59) "Đã tạo ${seconds} giây trước"
      else if (minutes <= 59) "Đã tạo ${minutes} phút trước"
      else if (hours <= 23) "Đã tạo ${hours} giờ trước"
      else if (hours >= 24 && hours <= 48) "Đã tạo hôm qua"
      else "Đã tạo vào ${dayOfMonth} thg ${monthValue}"
  }

val LocalDateTime.isOverTime: Boolean
get() {
  return this < LocalDateTime.now()
}