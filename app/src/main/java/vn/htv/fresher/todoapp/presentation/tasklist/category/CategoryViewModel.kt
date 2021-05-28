package vn.htv.fresher.todoapp.presentation.tasklist.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.category.DeleteCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.UpdateCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.DeleteTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListViewModel

class CategoryViewModel(
  private val deleteCategoryUseCase : DeleteCategoryUseCase,
  private val deleteTaskListUseCase : DeleteTaskListUseCase,
  private val getCategoryUseCase    : GetCategoryUseCase,
  private val getTaskListUseCase    : GetTaskListUseCase,
  private val saveTaskUseCase       : SaveTaskUseCase,
  private val updateTaskUseCase     : UpdateTaskUseCase,
  private val updateCategoryUseCase : UpdateCategoryUseCase
) : TaskListViewModel(
  saveTaskUseCase,
  updateTaskUseCase,
) {

  val deleteCategoryCompleted: LiveData<Boolean> get() = _deleteCategoryCompleted
  private val _deleteCategoryCompleted = MutableLiveData<Boolean>()

  val itemCategory: LiveData<CategoryModel> get() = _itemCategory
  private val _itemCategory = MutableLiveData<CategoryModel>()

  val updateCategoryCompleted: LiveData<Boolean> get() = _updateCategoryCompleted
  private val _updateCategoryCompleted = MutableLiveData<Boolean>()



  var categoryId: Int? = null

  override fun addTask(name: String) {
    val catId = categoryId ?: return
    
    val model = TaskModel(
      catId = catId,
      name  = name)

    saveTask(model)
  }

  fun deleteCategory() {
    val catId = categoryId          ?: return
    val model = itemCategory.value  ?: return

    val deleteCategoryObservable = deleteCategoryUseCase(model)
    val deleteTaskListObservable = deleteTaskListUseCase(catId)

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

  fun loadCategory() {
    val id = categoryId?.toLong() ?: return

    disposables += getCategoryUseCase(id)
      .subscribeBy(
        onSuccess = {
          _itemCategory.postValue(it)
          _title.postValue(it.name)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  override fun loadData() {
    loadCategory()
    loadTask()
  }

  private fun loadTask() {
    val id = categoryId ?: return

    disposables += getTaskListUseCase(id)
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it)
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun updateCategory(name: String) {
    val model = itemCategory.value?.copy(name = name) ?: return

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
}