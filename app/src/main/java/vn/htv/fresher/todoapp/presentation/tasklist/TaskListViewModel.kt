package vn.htv.fresher.todoapp.presentation.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel

abstract class TaskListViewModel(
  private val saveTaskUseCase    : SaveTaskUseCase,
  private val updateTaskUseCase  : UpdateTaskUseCase
) : BaseViewModel() {

  val addTaskCompleted: LiveData<Boolean> get() = _addTaskCompleted
  private val _addTaskCompleted = MutableLiveData<Boolean>()

  val itemList: LiveData<List<TaskModel>> get() = _itemList
  protected val _itemList = MutableLiveData<List<TaskModel>>()

  val updateTaskCompleted: LiveData<Boolean> get() = _updateTaskCompleted
  private val _updateTaskCompleted = MutableLiveData<Boolean>()

  val title: LiveData<String> get() = _title
  protected val _title = MutableLiveData<String>()

  // Abstract functions
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  abstract fun addTask(name: String)

  abstract fun loadData()

  // Public functions
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  fun saveTask(model: TaskModel) {
    disposables += saveTaskUseCase(model)
      .subscribeBy(
        onComplete = {
          _addTaskCompleted.postValue(true)
          Timber.i("saved task success $model")
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun updateFinishStateTask(model: TaskModel) {
    updateTask(model.copy(finished = !model.finished))
  }

  fun updateImportantTask(model: TaskModel) {
    updateTask(model.copy(important = !model.important))
  }

  // Private functions
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private fun updateTask(model: TaskModel) {
    disposables += updateTaskUseCase(model)
      .subscribeBy(
        onComplete = {
          _updateTaskCompleted.postValue(true)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }
}