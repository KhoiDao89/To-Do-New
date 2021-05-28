package vn.htv.fresher.todoapp.util.ext

import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime


val LocalDateTime.reminderTimeString: String
  get() {
    return "$hour:$minute"
  }

val LocalDateTime.taskCreatedAtString: String
  get() {
    val duration: Duration = Duration.between(this, LocalDateTime.now())

    val seconds = duration.seconds
    val minutes = duration.toMinutes()
    val hours   = duration.toHours()
    val day     = duration.toDays()

    val ytd: Long = 1

    return when {
      seconds <= 59   -> "Đã tạo $seconds giây trước"
      minutes <= 59   -> "Đã tạo $minutes phút trước"
      hours   <= 23   -> "Đã tạo $hours giờ trước"
      day     == ytd  -> "Đã tạo hôm qua"
      else            -> "Đã tạo vào $dayOfMonth tháng $monthValue"
    }
  }

val LocalDateTime.deadlineString: String
  get() {
    val duration: Duration = Duration.between(this, LocalDateTime.now())
    val day = duration.toDays()

    val yesterday : Long = 1
    val today     : Long = 0
    val tomorrow  : Long = -1

    return when (day) {
      yesterday -> "Hôm qua"
      today     -> "Hôm nay"
      tomorrow  -> "Ngày mai"
      else      -> "$dayOfMonth tháng $monthValue"
    }
  }