package vn.htv.fresher.todoapp.presentation.category

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

  var deleteCategoryCallback      : (() -> Unit)? = null
  var updateCategoryNameCallback  : (() -> Unit)? = null

  override fun init() {
    super.init()

    loadDataOnRecyclerView()
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
    when (item.getItemId()) {
      R.id.changeCategoryNameMenu -> updateCategoryNameCallback?.invoke()
      R.id.deleteCategoryMenu     -> deleteCategoryCallback?.invoke()
    }

    return super.onOptionsItemSelected(item)
  }

  override fun onResume() {
    super.onResume()

    loadDataOnRecyclerView()

    viewModel.addTaskCompleted.observe(this, Observer {
      if (!it) return@Observer
      loadDataOnRecyclerView()
    })

    viewModel.updateTaskCompleted.observe(this, Observer {
      if (!it) return@Observer
      loadDataOnRecyclerView()
    })

    viewModel.itemCategory.observe(this, {
      setToolbarTitle(it.name)
    })
  }

  fun loadDataOnRecyclerView() {
    viewModel.taskGroup = intent.getStringExtra(PARAM_EXTRA_TASK_GROUP)

    when (viewModel.taskGroup) {
      TaskGroup.MY_DAY.toString() -> {
        setToolbarTitle(getString(R.string.task_group_my_day))
        viewModel.loadAllTaskMyDay()
      }
      TaskGroup.IMPORTANT.toString() -> {
        setToolbarTitle(getString(R.string.task_group_important))
        viewModel.loadAllTaskImportant()
      }
      TaskGroup.DEADLINE.toString() -> {
        setToolbarTitle(getString(R.string.task_attribute_deadline))
        viewModel.loadAllTaskDeadline()
      }
      TaskGroup.ACTION.toString() -> {
        setToolbarTitle(getString(R.string.task_group_action))
        viewModel.loadAllTaskAction()
      }
      else -> {
        val catId = intent.getLongExtra(PARAM_EXTRA_CATEGORY_ID, 0)

        viewModel.categoryId = catId

        viewModel.loadCategory()
        viewModel.loadTask()
      }
    }
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
