package vn.htv.fresher.todoapp.util.ext

import org.threeten.bp.LocalDateTime


val LocalDateTime.timeString: String
  get() {
    return "${hour}:${minute}"
  }