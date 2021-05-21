package vn.htv.fresher.todoapp.presentation.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.category.*
import vn.htv.fresher.todoapp.domain.usecase.task.DeleteTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel
import vn.htv.fresher.todoapp.presentation.main.TaskGroup
import java.security.InvalidParameterException

class CategoryViewModel(
  private val deleteCategoryUseCase  : DeleteCategoryUseCase,
  private val deleteTaskListUseCase  : DeleteTaskListUseCase,
  private val getCategoryUseCase     : GetCategoryUseCase,
  private val getTaskListUseCase     : GetTaskListUseCase,
  private val saveTaskUseCase        : SaveTaskUseCase,
  private val updateTaskUseCase      : UpdateTaskUseCase,
  private val updateCategoryUseCase  : UpdateCategoryUseCase
) : BaseViewModel() {

  val addTaskCompleted: LiveData<Boolean> get() = _addTaskCompleted
  private val _addTaskCompleted = MutableLiveData<Boolean>()

  val deleteCategoryCompleted: LiveData<Boolean> get() = _deleteCategoryCompleted
  private val _deleteCategoryCompleted = MutableLiveData<Boolean>()

  val itemCategory: LiveData<CategoryModel> get() = _itemCategory
  private val _itemCategory = MutableLiveData<CategoryModel>()

  val itemList: LiveData<List<TaskModel>> get() = _itemList
  private val _itemList = MutableLiveData<List<TaskModel>>()

  val updateTaskCompleted: LiveData<Boolean> get() = _updateTaskCompleted
  private val _updateTaskCompleted = MutableLiveData<Boolean>()

  val updateCategoryCompleted: LiveData<Boolean> get() = _updateCategoryCompleted
  private val _updateCategoryCompleted = MutableLiveData<Boolean>()

  var categoryId  : Long?       = null
  var taskGroup   : TaskGroup?  = null

  fun loadCategory() {
    val id = categoryId ?: return

    disposables += getCategoryUseCase(id)
      .subscribeBy(
        onSuccess = {
          _itemCategory.postValue(it)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun loadData() {
    when {
      taskGroup   != null -> loadTaskAttribute()
      categoryId  != null -> {
        loadCategory()
        loadTask()
      }
    }
  }

  private fun loadTask() {
    val id = categoryId ?: return

    disposables += getTaskListUseCase(id.toInt())
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  private fun loadTaskAttribute() {
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

  fun updateCategory(categoryName: String) {
    val model = itemCategory.value?.copy(name = categoryName) ?: return

    disposables += updateCategoryUseCase(model)
      .subscribeBy(
        onComplete = {
          _updateCategoryCompleted.postValue(true)
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

  fun deleteCategory() {
    val catId = categoryId          ?: return
    val model = itemCategory.value  ?: return

    val deleteCategoryObservable = deleteCategoryUseCase(model)
    val deleteTaskListObservable = deleteTaskListUseCase(catId.toInt())

    disposables += deleteTaskListObservable.andThen(deleteCategoryObservable)
      .subscribeBy(
        onComplete = {
          _deleteCategoryCompleted.postValue(true)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun addTask(taskName: String) {
    val model = TaskModel(name = taskName)

    var taskModel = model.copy()

    when {
      taskGroup != null -> {
        when(taskGroup) {
          TaskGroup.MY_DAY    -> taskModel = model.copy(myDay = true)
          TaskGroup.IMPORTANT -> taskModel = model.copy(important = true)
          TaskGroup.DEADLINE  -> taskModel = model.copy(deadline = LocalDateTime.now())
          TaskGroup.ACTION    -> {}
        }
      }
      categoryId != null -> taskModel = model.copy(catId = categoryId?.toInt())
    }

    disposables += saveTaskUseCase(taskModel)
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
}
