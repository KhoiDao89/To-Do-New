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

    (safeActivity as? CategoryActivity)?.let {
      it.deleteCategoryCallback = {
        this.showConfirmDialog(
          title             = R.string.delete_title,
          message           = getString(R.string.delete_task_message, viewModel.itemCategory.value?.name),
          positiveName      = R.string.delete,
          positiveCallback  = {
            val model = viewModel.itemCategory.value ?: return@showConfirmDialog

            viewModel.deleteCategory(model)
          }
        )
      }
      it.updateCategoryNameCallback = {
        this.showInputDialog(
          title                 = R.string.new_name_category,
          prefill               = viewModel.itemCategory.value?.name,
          positiveName          = R.string.button_save,
          positiveTaskCallback  = { viewModel.updateCategoryName(it) }
        )
      }
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

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.itemList.observe(this, {
        taskAdapter.setItems(it)
    })

    viewModel.updateCategoryCompleted.observe(this, {
      viewModel.loadCategory()
    })

    viewModel.deleteCategoryCompleted.observe(this, {
      safeActivity.onBackPressed()
      viewModel.loadCategory()
    })
  }

  inner class EventAddTask {
    fun onNewTask() {
      this@CategoryFragment.showInputDialog(
        title                = R.string.new_task,
        hint                 = R.string.new_task_hint,
        positiveName         = R.string.button_create_task,
        positiveTaskCallback = { viewModel.addNewTask(it) }
      )
    }
  }

  companion object {
    fun newInstance() = CategoryFragment()
  }
}
