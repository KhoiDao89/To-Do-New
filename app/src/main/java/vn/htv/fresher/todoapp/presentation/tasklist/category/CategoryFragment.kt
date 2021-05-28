package vn.htv.fresher.todoapp.presentation.tasklist.category

import kotlinx.android.synthetic.main.fragment_task_list.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListFragment
import vn.htv.fresher.todoapp.util.ext.showConfirmDialog
import vn.htv.fresher.todoapp.util.ext.showInputDialog

class CategoryFragment : TaskListFragment<CategoryViewModel>() {
  override val backgroundTintResId = R.color.bg_action_button

  override val viewModel by sharedViewModel<CategoryViewModel>()

  override fun init() {
    super.init()

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
        title         = R.string.new_name_category,
        text          = viewModel.itemCategory.value?.name,
        positiveName  = R.string.button_save,
        saveCallback  = { name ->
          viewModel.updateCategory(name)
        }
      )
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.deleteCategoryCompleted.observe(this, {
      safeActivity.onBackPressed()
    })

    viewModel.updateCategoryCompleted.observe(this, {
      if (!it) return@observe
      viewModel.loadCategory()
    })
  }

  /**
   * CategoryFragment static definition
   */
  companion object {
    fun newInstance() = CategoryFragment()
  }
}