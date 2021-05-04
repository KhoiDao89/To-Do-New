package vn.htv.fresher.todoapp.presentation.taskdetail

import android.content.Context
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
import vn.htv.fresher.todoapp.data.db.entity.Task
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase

enum class TaskAttributeEnum {
  MY_DAY,
  IMPORTANT,
  DEADLINE,
  NOTE;
}

sealed class TaskDetailItem {
  data class SubTask(val model: SubTaskModel): TaskDetailItem()

  data class TaskAttribute(
    val model: SubTaskModel,
    val attribute: TaskAttributeEnum
    ): TaskDetailItem()
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
}