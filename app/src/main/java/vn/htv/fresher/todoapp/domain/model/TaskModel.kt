package vn.htv.fresher.todoapp.domain.model

import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskAttributeEnum

data class TaskModel(
  val id         : Int? = null,
  val catId      : Int? = null,
  val name       : String,
  val finished   : Boolean = false,
  val deadline   : LocalDateTime? = null,
  val myDay      : Boolean = false,
  val important  : Boolean = false,
  val reminder   : LocalDateTime? = null,
  val repeat     : Int? = null,
  val createdAt  : LocalDateTime = LocalDateTime.now(),
  val note       : String? = null
) {
  fun getAttributeState(attribute: TaskAttributeEnum): Boolean {
    return when(attribute) {
      TaskAttributeEnum.MY_DAY    -> myDay
      TaskAttributeEnum.REMINDER  -> reminder != null
      TaskAttributeEnum.DEADLINE  -> deadline != null
      TaskAttributeEnum.REPEAT    -> repeat   != null
    }
  }

  val deadlineState get() = if (deadline == null) false else deadline < LocalDateTime.now()
}