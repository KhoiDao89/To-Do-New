package vn.htv.fresher.todoapp.presentation.taskdetail

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.subtask.GetSubTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.subtask.SaveSubTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel
import io.reactivex.functions.BiFunction
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.data.db.entity.Task
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.util.ext.timeString

enum class TaskAttributeEnum {
  MY_DAY,
  REMINDER,
  DEADLINE,
  REPEAT;

  val attributeName: Int
    @StringRes get() = when (this) {
      MY_DAY    -> R.string.task_attribute_my_day
      REMINDER  -> R.string.task_attribute_reminder
      DEADLINE  -> R.string.task_attribute_deadline
      REPEAT    -> R.string.task_attribute_repeat
    }

//  val attributeNameSet: Int
//    @StringRes get() = when (this) {
//      MY_DAY    -> R.string.task_attribute_set_my_day
//      REMINDER  -> R.string.task_attribute_set_reminder
//      DEADLINE  -> R.string.task_attribute_set_deadline
//      REPEAT    -> R.string.task_attribute_set_repeat
//    }

  val attributeIcon: Int
    @DrawableRes get() = when (this) {
      MY_DAY    -> R.drawable.ic_my_day
      REMINDER  -> R.drawable.ic_reminder
      DEADLINE  -> R.drawable.ic_deadline
      REPEAT    -> R.drawable.ic_repeat
    }

  fun getName(setState: Boolean, model: TaskModel, context: Context): String {
    if (!setState) return context.getString(attributeName)

    return getNameInSetState(model, context)
  }

  fun getNameInSetState(model: TaskModel, context: Context): String {
    return when(this) {
      MY_DAY    -> context.getString(R.string.task_attribute_set_my_day)
      REMINDER  -> context.getString(R.string.task_attribute_set_reminder, model.reminder?.timeString)
      DEADLINE  -> context.getString(R.string.task_attribute_set_my_day)
      REPEAT    -> context.getString(R.string.task_attribute_set_my_day)
    }
  }
}

enum class SubItemType(val value: Int){
  SUBTASK_ITEM(0),
  NEXT_STEP(1),
  ATTRIBUTE(2),
  NOTE(3);

  companion object {
    fun from(value: Int) = values().first { it.value == value }
  }
}

sealed class TaskDetailItem(val type: SubItemType) {
  data class SubTask(val model: SubTaskModel): TaskDetailItem(SubItemType.SUBTASK_ITEM)

  object NextStep: TaskDetailItem(SubItemType.NEXT_STEP)

  data class TaskAttribute(
    val model     : TaskModel,
    val attribute : TaskAttributeEnum
    ): TaskDetailItem(SubItemType.ATTRIBUTE)

  object Note: TaskDetailItem(SubItemType.NOTE)
}

class TaskDetailViewModel(
  private val getTaskUseCase        : GetTaskUseCase,
  private val getSubTaskListUseCase : GetSubTaskListUseCase,
) : BaseViewModel() {

  val taskDetailItem: LiveData<List<TaskDetailItem>> get() = _taskDetailItem
  private val _taskDetailItem = MutableLiveData<List<TaskDetailItem>>()

  fun loadData() {
    val getTaskObserable    = getTaskUseCase(1)
    val getSubTaskObserable = getSubTaskListUseCase(1)

    val zipper = BiFunction<TaskModel, List<SubTaskModel>, List<TaskDetailItem>>{ task, subtasks ->
      val list = mutableListOf<TaskDetailItem>()

      val subTaskItems = subtasks.map { subtask ->
        TaskDetailItem.SubTask( subtask )
      }
      list.addAll(subTaskItems)

      list.add(TaskDetailItem.NextStep)

      val listAttributes = generateTaskAttribute(task)
      list.addAll(listAttributes)
      list.add(TaskDetailItem.Note)
      list
    }

    disposables += Single.zip(getTaskObserable, getSubTaskObserable, zipper)
      .subscribeBy (
        onSuccess = {
          _taskDetailItem.postValue(it)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  private fun generateTaskAttribute(taskAttribure: TaskModel): List<TaskDetailItem>{
    val list = mutableListOf<TaskDetailItem>()
    val items = TaskAttributeEnum.values().map { attribites ->
      TaskDetailItem.TaskAttribute(taskAttribure, attribites)
    }
    list.addAll(items)
    return list
  }
}