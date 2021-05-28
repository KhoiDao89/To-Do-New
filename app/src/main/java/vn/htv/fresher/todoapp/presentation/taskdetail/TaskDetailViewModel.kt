package vn.htv.fresher.todoapp.presentation.taskdetail

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Single
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel
import io.reactivex.functions.BiFunction
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.domain.usecase.subtask.*
import vn.htv.fresher.todoapp.domain.usecase.task.DeleteTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.util.ext.taskCreatedAtString
import vn.htv.fresher.todoapp.util.ext.deadlineString
import vn.htv.fresher.todoapp.util.ext.reminderTimeString

enum class TaskAttributeEnum {
  MY_DAY,
  REMINDER,
  DEADLINE,
  REPEAT;

  private val attributeName: Int
    @StringRes get() = when (this) {
      MY_DAY    -> R.string.task_attribute_my_day
      REMINDER  -> R.string.task_attribute_reminder
      DEADLINE  -> R.string.task_attribute_deadline
      REPEAT    -> R.string.task_attribute_repeat
    }

  val attributeIcon: Int
    @DrawableRes get() = when (this) {
      MY_DAY    -> R.drawable.ic_my_day
      REMINDER  -> R.drawable.ic_reminder
      DEADLINE  -> R.drawable.ic_deadline_sub
      REPEAT    -> R.drawable.ic_repeat
    }

  fun getName(setState: Boolean, model: TaskModel, context: Context): String {
    if (!setState) return context.getString(attributeName)

    return getNameInSetState(model, context)
  }

  private fun getNameInSetState(model: TaskModel, context: Context): String {
    return when(this) {
      MY_DAY    -> context.getString(R.string.task_attribute_set_my_day)
      REMINDER  -> context.getString(R.string.task_attribute_set_reminder, model.reminder?.reminderTimeString)
      DEADLINE  -> context.getString(R.string.task_attribute_set_deadline, model.deadline?.deadlineString)
      REPEAT    -> context.getString(R.string.task_attribute_set_repeat)
    }
  }

  fun getColor(isSet: Boolean, deadline: Boolean? = null): Int {
    return when(this) {
      MY_DAY, REMINDER, REPEAT -> if (isSet) R.color.dark_blue else R.color.dark_gray
      DEADLINE -> if (deadline == true) R.color.dark_red else if (isSet) R.color.dark_blue else R.color.dark_gray
    }
  }
}

enum class SubItemType(val value: Int) {
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

  data class Note(val model: TaskModel): TaskDetailItem(SubItemType.NOTE)
}

class TaskDetailViewModel(
  private val deleteSubTaskListUseCase  : DeleteSubTaskListUseCase,
  private val deleteSubTaskUseCase      : DeleteSubTaskUseCase,
  private val deleteTaskUseCase         : DeleteTaskUseCase,
  private val getSubTaskListUseCase     : GetSubTaskListUseCase,
  private val getTaskUseCase            : GetTaskUseCase,
  private val saveSubTaskUseCase        : SaveSubTaskUseCase,
  private val updateSubTaskUseCase      : UpdateSubTaskUseCase,
  private val updateTaskUseCase         : UpdateTaskUseCase
) : BaseViewModel() {

  val taskDetailItem: LiveData<List<TaskDetailItem>> get() = _taskDetailItem
  private val _taskDetailItem = MutableLiveData<List<TaskDetailItem>>()

  val task : LiveData<TaskModel> get() = _task
  private val _task = MutableLiveData<TaskModel>()

  val taskFinishIcon : LiveData<Int> get() = Transformations.map(_task) {
    if (it.finished) R.drawable.ic_finished else R.drawable.ic_not_finish
  }

  val taskImportantIcon : LiveData<Int> get() = Transformations.map(_task) {
    if (it.important) R.drawable.ic_important_blue else R.drawable.ic_important_gray
  }

  val taskCreatedAt : LiveData<String> get() = Transformations.map(_task) {
    it.createdAt.taskCreatedAtString
  }

  var taskId: Int? = null

  fun loadData() {
    val id = taskId ?: return

    val getTaskObservable    = getTaskUseCase(id)
    val getSubTaskObservable = getSubTaskListUseCase(id)

    val zipper = BiFunction<TaskModel, List<SubTaskModel>, List<TaskDetailItem>> { task, subtasks ->

      _task.postValue(task)

      val list = mutableListOf<TaskDetailItem>()

      val subTaskItems = subtasks.map { subtask ->
        TaskDetailItem.SubTask( subtask )
      }
      list.addAll(subTaskItems)

      list.add(TaskDetailItem.NextStep)

      val listAttributes = generateTaskAttribute(task)
      list.addAll(listAttributes)

      list.add(TaskDetailItem.Note(task))
      list
    }

    disposables += Single.zip(getTaskObservable, getSubTaskObservable, zipper)
      .subscribeBy (
        onSuccess = {
          _taskDetailItem.postValue(it)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  private fun generateTaskAttribute(taskAttribute: TaskModel): List<TaskDetailItem> {
    val list = mutableListOf<TaskDetailItem>()

    val items = TaskAttributeEnum.values().map { attribute ->
      TaskDetailItem.TaskAttribute(taskAttribute, attribute)
    }
    list.addAll(items)
    return list
  }

  fun addSubTask(taskId: Int, subTaskName: String) {
    val model = SubTaskModel(
      taskId    = taskId,
      name      = subTaskName
    )

    disposables += saveSubTaskUseCase(model)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Saved $model from Database")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun finishedTask() {
    val task = _task.value ?: return

    val updatedFinishTask = task.copy(
      finished = !task.finished
    )

    disposables += updateTaskUseCase(updatedFinishTask)
      .subscribeBy (
        onComplete = {
          loadData()
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun importantTask() {
    val task = _task.value ?: return

    val updatedImportantTask = task.copy(
      important = !task.important
    )

    disposables += updateTaskUseCase(updatedImportantTask)
      .subscribeBy (
        onComplete = {
          loadData()
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun deleteTask() {
    val task    = _task.value ?: return
    val taskId  = task.id     ?: return

    val deleteSubTaskListObservable = deleteSubTaskListUseCase(taskId)
    val deleteTaskObservable        = deleteTaskUseCase(task)

    disposables += deleteSubTaskListObservable.andThen(deleteTaskObservable)
      .subscribeBy(
        onComplete = {
          Timber.i("Deleted $task from Database")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun myDayTask(model: TaskModel) {
    val updateMyDayTask = model.copy(
      myDay = !model.myDay
    )

    disposables += updateTaskUseCase(updateMyDayTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Updated my day $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun reminderTask(model: TaskModel, reminder: LocalDateTime) {
    val updateReminderTask = model.copy(
      reminder = reminder
    )

    disposables += updateTaskUseCase(updateReminderTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Updated reminder $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun removeReminderTask(model: TaskModel) {
    val removeReminderTask = model.copy(
      reminder = null
    )

    disposables += updateTaskUseCase(removeReminderTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Remove reminder $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun deadlineTask(model: TaskModel, deadline: LocalDateTime) {
    val updateDeadlineTask = model.copy(
      deadline = deadline
    )

    disposables += updateTaskUseCase(updateDeadlineTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Updated deadline $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun removeDeadlineTask(model: TaskModel) {
    val removeDeadlineTask = model.copy(
      deadline  = null,
      repeat    = null
    )

    disposables += updateTaskUseCase(removeDeadlineTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Remove deadline $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun repeatTask(model: TaskModel, repeat: Int, deadline: LocalDateTime) {
    val updateRepeatTask = model.copy(
      repeat    = repeat,
      deadline  = deadline
    )

    disposables += updateTaskUseCase(updateRepeatTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Updated repeat $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun removeRepeatTask(model: TaskModel) {
    val removeRepeatTask = model.copy(
      repeat = null
    )

    disposables += updateTaskUseCase(removeRepeatTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Remove repeat $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun noteTask(note: String?) {
    val model = _task.value ?: return

    val updateNoteTask = model.copy(
      note = note
    )

    disposables += updateTaskUseCase(updateNoteTask)
      .subscribeBy(
        onComplete = {
          loadData()
          Timber.i("Updated note $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun updateFinishStateSubTask(model: SubTaskModel) {
    val updatedFinishedSubTask = model.copy(
      finished = !model.finished
    )

    disposables += updateSubTaskUseCase(updatedFinishedSubTask)
      .subscribeBy (
        onComplete = {
          loadData()
          Timber.i("Updated finished $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun deleteSubTask(model: SubTaskModel) {
    disposables += deleteSubTaskUseCase(model)
      .subscribeBy (
        onComplete = {
          loadData()
          Timber.i("Deleted $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }
}