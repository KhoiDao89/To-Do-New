package vn.htv.fresher.todoapp.presentation.category

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.common.BaseActivity
import vn.htv.fresher.todoapp.presentation.main.TaskGroup

class CategoryActivity : BaseActivity() {

  private val viewModel by viewModel<CategoryViewModel>()

  override val fragment  : Fragment
    get() = CategoryFragment.newInstance()

  override val layoutId  : Int
    get() = R.layout.activity_category

  var deleteCategoryCallback  : (() -> Unit)? = null
  var updateCategoryCallback  : (() -> Unit)? = null

  override fun init() {
    super.init()

    viewModel.taskGroup = TaskGroup.from(intent.getStringExtra(PARAM_EXTRA_TASK_GROUP))

    when {
      viewModel.taskGroup != null -> {
        val taskAttributeId = viewModel.taskGroup?.groupName ?: return
        setToolbarTitle(getString(taskAttributeId))
      }
      else -> viewModel.categoryId = intent.getLongExtra(PARAM_EXTRA_CATEGORY_ID, 0)
    }

    viewModel.loadData()
  }

  override fun initUi() {
    super.initUi()

    showBackButton()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    if (viewModel.categoryId != null) menuInflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.changeCategoryNameMenu -> updateCategoryCallback?.invoke()
      R.id.deleteCategoryMenu     -> deleteCategoryCallback?.invoke()
    }

    return super.onOptionsItemSelected(item)
  }

  companion object {
    const val PARAM_EXTRA_CATEGORY_ID = "CATEGORY"
    const val PARAM_EXTRA_TASK_GROUP  = "TASK_GROUP"

    fun start(activity: AppCompatActivity, taskGroup: TaskGroup? = null, catId: Long? = null) {
      val intent = Intent(activity, CategoryActivity::class.java)

      intent.apply {
        putExtra(PARAM_EXTRA_TASK_GROUP, taskGroup?.name)
        putExtra(PARAM_EXTRA_CATEGORY_ID, catId)
      }

      activity.startActivity(intent)
    }
  }
}
