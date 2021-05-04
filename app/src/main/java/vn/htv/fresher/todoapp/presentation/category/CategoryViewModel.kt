package vn.htv.fresher.todoapp.presentation.category

import android.widget.RadioButton
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.data.db.entity.Task
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.domain.usecase.category.DeleteCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryListUseCase
import vn.htv.fresher.todoapp.domain.usecase.category.SaveCategoryUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.DeleteTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.GetTaskListUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.SaveTaskUseCase
import vn.htv.fresher.todoapp.domain.usecase.task.UpdateTaskUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseViewModel
import vn.htv.fresher.todoapp.presentation.main.MainItemModel
import vn.htv.fresher.todoapp.presentation.main.MainItemType

//enum class MainTaskItemType(val v: Int) {
//  NOT_FINISH(0),
//  FINISHED_STATE(1);
//  companion object {
//    fun from(value: Int) = values().first { it.v == value }
//  }
//}
//
//sealed class MainTaskItem(val type: MainTaskItemType) {
//  data class Item(val model: MainTaskItemModel): MainTaskItem(MainTaskItemType.NOT_FINISH)
//
//  object TaskFinished: MainTaskItem(MainTaskItemType.FINISHED_STATE)
//}
//
//data class MainTaskItemModel(
//  val finished    : Boolean,
//  val nameTask    : String,
//  val important  : Boolean
//) {
//
//}

class CategoryViewModel(
    //private val getCategoryListUseCase: GetCategoryListUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    //private val saveCategoryUseCase: SaveCategoryUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : BaseViewModel() {

//  val setImportant: LiveData<Boolean> get() = _setImportant
//  private _val _setImportant = MutableLiveData<Boolean>()

  val updateImportantCompleted: LiveData<Boolean> get() = _updateImportantCompleted
  private val _updateImportantCompleted = MutableLiveData<Boolean>()

//  val updateFinishedCompleted: LiveData<Boolean> get() = _updateFinishedCompleted
//  private val _updateFinishedCompleted = MutableLiveData<Boolean>()

  val itemList: LiveData<List<TaskModel>> get() = _itemList
  private val _itemList = MutableLiveData<List<TaskModel>>()

//  val catItemList: LiveData<List<CategoryModel>> get() = _catItemList
//  private val _catItemList = MutableLiveData<List<CategoryModel>>()
 fun loadData() {
//  val getTaskObservable = getTaskListUseCase()
//  val zipper = BiFunction<List<TaskModel>, List<TaskModel>, List<TaskModel>> { tasks1, tasks2 ->
//    val list = mutableListOf<TaskModel>()
//
//    list.add(MainTaskItem.TaskFinished)
//
//    val taskItems = tasks1.map { t ->
//      MainTaskItem.Item(
//        MainTaskItemModel(
//          finished = t.finished,
//          nameTask = t.name,
//          important = t.important
//        )
//      )
//    }
//    list.addAll(taskItems)
//    list
//  }
    disposables += getTaskListUseCase()
      .subscribeBy(
        onSuccess = {
          _itemList.postValue(it)
        },
        onError = {
          Timber.e("error")
        }
      )
  }
//  disposables += getCategoryListUseCase()
//      .subscribeBy(
//           onSuccess = {
//            _catItemList.postValue(it)
//          },
//          onError = {
//            Timber.e("error")
//          }
//      )

//  fun updateComplete(model: TaskModel) {
//    updateTask(model.copy(finished = !model.finished))
//  }

  fun updateImportant(model: TaskModel) {
    updateTask(model.copy(important = !model.important))
  }

//  fun updateTaskFinished(model: TaskModel) {
//    disposables += updateTaskUseCase(model)
//      .subscribeBy(
//        onComplete = {
//          _updateFinishedCompleted.postValue(true)
//        },
//        onError = {
//          Timber.e("error")
//        }
//      )
//  }
  fun updateTask(model: TaskModel) {

    disposables += updateTaskUseCase(model)
        .subscribeBy(
            onComplete = {
              _updateImportantCompleted.postValue(true)
            },
            onError = {
              Timber.e("error")
            }
        )
  }
//  fun saveTaskList(): List<TaskModel>{
//    var list = ArrayList<TaskModel>()
//    list.add(TaskModel(
//        id = 1,
//        catId = 1,
//        name = "",
//        finished = true,
//        deadline = LocalDateTime.now(),
//        myDay = true,
//        important = true,
//        reminder = LocalDateTime.now(),
//        repeat = 1,
//        createdAt = LocalDateTime.now(),
//        note = ""))
//    return  list
//  }
//  fun insertCategory() {
//    val model = CategoryModel(
//        name = "category",
//        id = 1,
//        icon = "",
//        createdAt = LocalDateTime.now()
//    )
//  disposables += saveCategoryUseCase(model)
//      .subscribeBy(
//          onComplete = {
//            Timber.i("saved task success")
//          },
//          onError = {
//            Timber.e(" error")
//          }
//      )
//  }
  fun insertTask() {
  val model = TaskModel(
      name = "task",
      catId = 1,
      finished = false,
      deadline = LocalDateTime.now(),
      myDay = true,
      important = true,
      reminder = LocalDateTime.now(),
      repeat = 1,
      createdAt = LocalDateTime.now(),
      note = ""
  )
  disposables += saveTaskUseCase(model)
      .subscribeBy(
          onComplete = {
            Timber.i("saved task success")
          },
          onError =
          {
            Timber.e(" error")
          }
      )
  }
}