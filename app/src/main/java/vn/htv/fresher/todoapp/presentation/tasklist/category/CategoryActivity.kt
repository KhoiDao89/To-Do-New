package vn.htv.fresher.todoapp.presentation.tasklist.category

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListActivity

class CategoryActivity : TaskListActivity<CategoryViewModel>() {
  override var backgroundResId: Int = 0

  override val fragment: Fragment
    get() = CategoryFragment.newInstance()

  override val viewModel by viewModel<CategoryViewModel>()

  var deleteCategoryCallback  : (() -> Unit)? = null
  var updateCategoryCallback  : (() -> Unit)? = null

  override fun init() {
    val catId = intent.getIntExtra(PARAM_EXTRA_CATEGORY_ID, 0)
    viewModel.categoryId = catId

    super.init()
  }

  override fun setBackground() {
    backgroundResId = R.color.bg_action

    super.setBackground()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.updateCategoryNameMenu -> updateCategoryCallback?.invoke()
      R.id.deleteCategoryMenu     -> deleteCategoryCallback?.invoke()
    }

    return super.onOptionsItemSelected(item)
  }

  /**
   * CategoryActivity static definition
   */
  companion object {
    private const val PARAM_EXTRA_CATEGORY_ID = "param_extra_category_id"

    fun start(activity: AppCompatActivity, catId: Int) {
      val intent = Intent(activity, CategoryActivity::class.java).apply {
        putExtra(PARAM_EXTRA_CATEGORY_ID, catId)
      }

      activity.startActivity(intent)
    }
  }
}