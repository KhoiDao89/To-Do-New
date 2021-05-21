package vn.htv.fresher.todoapp.domain.model

import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R

data class SubTaskModel(
  val id         : Int? = null,
  val taskId     : Int,
  val name       : String,
  val finished   : Boolean = false,
  val createdAt  : LocalDateTime = LocalDateTime.now()
) {
  val finishedSubTask: Int = if (finished) R.drawable.ic_finished else R.drawable.ic_not_finish
}