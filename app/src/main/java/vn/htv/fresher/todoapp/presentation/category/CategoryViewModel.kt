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
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel

class CategoryViewModel(
  private val deleteCategoryUseCase  : DeleteCategoryUseCase,
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

  var categoryId: Long? = null

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

  fun loadTask() {
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

  fun updateFinished(model: TaskModel) {
    updateTask(model.copy(finished = !model.finished))
  }

  fun updateImportant(model: TaskModel) {
    updateTask(model.copy(important = !model.important))
  }

  fun updateTask(model: TaskModel) {
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

  fun updateCategory(model: CategoryModel) {
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

  fun deleteCategory(model: CategoryModel) {
    disposables += deleteCategoryUseCase(model)
      .subscribeBy(
        onComplete = {
          _deleteCategoryCompleted.postValue(true)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun addNewTask(catId: Int, taskName: String) {
    val model = TaskModel(
      catId     = catId,
      name      = taskName,
      createdAt = LocalDateTime.now()
    )

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
}
