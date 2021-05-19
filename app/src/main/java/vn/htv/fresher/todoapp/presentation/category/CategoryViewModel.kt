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
import vn.htv.fresher.todoapp.domain.usecase.task.DeleteTaskByCatIdUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel
import vn.htv.fresher.todoapp.presentation.main.TaskGroup

class CategoryViewModel(
  private val deleteCategoryUseCase     : DeleteCategoryUseCase,
  private val deleteTaskByCatIdUseCase  : DeleteTaskByCatIdUseCase,
  private val getCategoryUseCase        : GetCategoryUseCase,
  private val getTaskListUseCase        : GetTaskListUseCase,
  private val saveTaskUseCase           : SaveTaskUseCase,
  private val updateTaskUseCase         : UpdateTaskUseCase,
  private val updateCategoryUseCase     : UpdateCategoryUseCase
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

  var categoryId  : Long?   = null
  var taskGroup   : String? = null

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

  fun loadAllTaskMyDay() {
    disposables += getTaskListUseCase()
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it.filter { it.myDay })
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun loadAllTaskImportant() {
    disposables += getTaskListUseCase()
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it.filter { it.important })
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun loadAllTaskDeadline() {
    disposables += getTaskListUseCase()
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it.filter { it.deadline != null })
        },
        onError = {
          Timber.e(it.toString())
        }
      )
  }

  fun loadAllTaskAction() {
    disposables += getTaskListUseCase()
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it.filter { it.catId == null })
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

  fun updateCategoryName(title: String) {
    val catId = categoryId ?: return

    val model = CategoryModel(
      name       = title,
      id         = catId.toInt(),
      createdAt  = LocalDateTime.now()
    )

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
    val id = model.id ?: return

    disposables += deleteTaskByCatIdUseCase(id)
      .subscribeBy(
        onComplete = {
          Timber.i("Deleted all tasks have catId = $id")
        },
        onError = {
          Timber.e(it.toString())
        }
      )

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

  fun addNewTask(taskName: String) {
    if (taskGroup != null) {
      val model = TaskModel(
        name      = taskName,
        createdAt = LocalDateTime.now()
      )

      when (taskGroup) {
        TaskGroup.MY_DAY.toString() -> {
          val myDayModel = model.copy( myDay = true )

          disposables += saveTaskUseCase(myDayModel)
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
        TaskGroup.IMPORTANT.toString() -> {
          val importantModel = model.copy( important = true )

          disposables += saveTaskUseCase(importantModel)
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
        TaskGroup.DEADLINE.toString() -> {
          val deadlineModel = model.copy( deadline = LocalDateTime.now() )

          disposables += saveTaskUseCase(deadlineModel)
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
        TaskGroup.ACTION.toString() -> {
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
    }

    if (categoryId != null) {
      val model = TaskModel(
        catId     = categoryId?.toInt(),
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
}
