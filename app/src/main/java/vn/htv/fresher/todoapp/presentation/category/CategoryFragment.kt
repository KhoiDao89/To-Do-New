package vn.htv.fresher.todoapp.presentation.category

import kotlinx.android.synthetic.main.fragment_category.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentCategoryBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailActivity
import vn.htv.fresher.todoapp.util.ext.showConfirmDialog
import vn.htv.fresher.todoapp.util.ext.showInputDialog

class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {

  private val viewModel by sharedViewModel<CategoryViewModel>()

  override val layoutId: Int
    get() = R.layout.fragment_category

  private val taskAdapter by lazy {
    TaskAdapter(
      finishedCallback    = { viewModel.updateFinishStateTask(it) },
      importantCallback   = { viewModel.updateImportantTask(it) },
      taskDetailCallback  = { TaskDetailActivity.start(safeActivity, it) }
    )
  }

  override fun init() {
    super.init()

    binding.event = EventAddTask()

    val categoryActivity = safeActivity as? CategoryActivity ?: return

    categoryActivity.deleteCategoryCallback = {
      this.showConfirmDialog(
        title             = R.string.delete_title,
        message           = getString(R.string.delete_task_message, viewModel.itemCategory.value?.name),
        positiveName      = R.string.delete,
        positiveCallback  = { viewModel.deleteCategory() }
      )
    }

    categoryActivity.updateCategoryCallback = {
      this.showInputDialog(
        title                 = R.string.new_name_category,
        text                  = viewModel.itemCategory.value?.name,
        positiveName          = R.string.button_save,
        positiveCallback  = { categoryName ->
          viewModel.updateCategory(categoryName)
        }
      )
    }
  }

  override fun initUi() {
    super.initUi()

    taskRecycleView.apply {
      adapter = taskAdapter
      addItemDecoration(DefaultItemDecoration(
        resources.getDimensionPixelSize(R.dimen.layout_margin_start_end),
        resources.getDimensionPixelSize(R.dimen.layout_margin_top_bottom) ))
    }
  }

  override fun onResume() {
    super.onResume()

    viewModel.loadData()
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.addTaskCompleted.observe(this,{
      if (!it) return@observe
      viewModel.loadData()
    })

    viewModel.deleteCategoryCompleted.observe(this, {
      safeActivity.onBackPressed()
      viewModel.loadCategory()
    })

    viewModel.itemList.observe(this, {
      taskAdapter.setItems(it)
    })

    viewModel.updateCategoryCompleted.observe(this, {
      if (!it) return@observe
      viewModel.loadCategory()
    })

    viewModel.updateTaskCompleted.observe(this, {
      if (!it) return@observe
      viewModel.loadData()
    })

    viewModel.itemCategory.observe(this, {
      (safeActivity as? CategoryActivity)?.setToolbarTitle(it.name)
    })
  }

  inner class EventAddTask {
    fun onNewTask() {
      this@CategoryFragment.showInputDialog(
        title                = R.string.new_task,
        hint                 = R.string.new_task_hint,
        positiveName         = R.string.button_create_task,
        positiveCallback = { viewModel.addNewTask(it) }
      )
    }
  }

  companion object {
    fun newInstance() = CategoryFragment()
  }
}
