package vn.htv.fresher.todoapp.presentation.category

import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentCategoryBinding
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailActivity
import vn.htv.fresher.todoapp.util.ext.showConfirmDialog
import vn.htv.fresher.todoapp.util.ext.showInputDialog

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

  private val viewModel by sharedViewModel<CategoryViewModel>()

  override val layoutId: Int
    get() = R.layout.fragment_category

  private val taskAdapter by lazy {
    TaskAdapter(
      importantCallback = {
        viewModel.updateImportant(it)
      },
      finishedCallback = {
        viewModel.updateFinished(it)
      },
      taskDetailCallback = {
        TaskDetailActivity.start(safeActivity, it)
      }
    )
  }

  override fun init() {
    super.init()
    viewModel.loadCategory()
    viewModel.loadTask()
    binding.event = EventAddTask()
  }

  override fun initUi() {
    super.initUi()
    taskRecycleView.apply {
      adapter = taskAdapter
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()
    viewModel.itemList.observe(this, {
        taskAdapter.setItems(it)
    })

    viewModel.updateTaskCompleted.observe(this, {
      viewModel.loadTask()
    })

    viewModel.updateCategoryCompleted.observe(this, {
      viewModel.loadCategory()
    })

    viewModel.deleteCategoryCompleted.observe(this, {
      safeActivity.onBackPressed()
      viewModel.loadCategory()
    })

    viewModel.itemCategory.observe(this, {
      safeActivity.supportActionBar?.title = it.name
    })

    viewModel.addTaskCompleted.observe(this@CategoryFragment, Observer {
      if (!it) return@Observer
      viewModel.loadTask()
    })
  }

  override fun onResume() {
    super.onResume()

    viewModel.loadTask()
  }

  inner class EventAddTask() {
    fun onNewTask() {
      this@CategoryFragment.showInputDialog(
        title                = R.string.new_task,
        hint                 = R.string.new_task_hint,
        positiveName         = R.string.button_create_task,
        positiveTaskCallback = { taskName ->
          val catId = viewModel.categoryId?.toInt() ?: return@showInputDialog

          viewModel.addNewTask(catId, taskName)
        }
      )
    }
  }

  companion object {
    fun newInstance() = CategoryFragment()
  }
}
