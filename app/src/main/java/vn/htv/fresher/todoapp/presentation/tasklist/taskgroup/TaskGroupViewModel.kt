package vn.htv.fresher.todoapp.presentation.tasklist.taskgroup

import android.content.Context
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.main.TaskGroup
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListViewModel
import java.security.InvalidParameterException

class TaskGroupViewModel(
  private val context             : Context,
  private val getTaskListUseCase  : GetTaskListUseCase,
  private val saveTaskUseCase     : SaveTaskUseCase,
  private val updateTaskUseCase   : UpdateTaskUseCase
) : TaskListViewModel(
  saveTaskUseCase,
  updateTaskUseCase,
) {

  var taskGroup: TaskGroup? = null
  set(value) {
    field = value
    value?.let {
      _title.value = context.getString(it.groupName)
    }
  }

  override fun addTask(name: String) {
    val model = TaskModel(name = name)

    var taskModel = model.copy()

    when (taskGroup) {
      TaskGroup.MY_DAY    -> taskModel = model.copy(myDay = true)
      TaskGroup.IMPORTANT -> taskModel = model.copy(important = true)
      TaskGroup.DEADLINE  -> taskModel = model.copy(deadline = LocalDateTime.now())
      TaskGroup.ACTION    -> {}
    }

    saveTask(taskModel)
  }

  override fun loadData() {
    disposables += getTaskListUseCase()
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it.filter { taskModel ->
            when(taskGroup) {
              TaskGroup.MY_DAY     -> taskModel.myDay
              TaskGroup.IMPORTANT  -> taskModel.important
              TaskGroup.DEADLINE   -> taskModel.deadline != null
              TaskGroup.ACTION     -> taskModel.catId == null
              else                 -> throw InvalidParameterException("It is not TaskGroup")
            }
          })
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }
}