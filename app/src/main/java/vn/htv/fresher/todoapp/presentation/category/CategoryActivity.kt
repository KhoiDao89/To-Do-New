package vn.htv.fresher.todoapp.presentation.category

import android.annotation.SuppressLint
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.presentation.common.BaseActivity
import vn.htv.fresher.todoapp.presentation.main.TaskGroup

class CategoryActivity : BaseActivity() {

  private val viewModel by viewModel<CategoryViewModel>()

  override val fragment  : Fragment
    get() = CategoryFragment.newInstance()

  override val layoutId  : Int
    get() = R.layout.activity_category

  override fun init() {
    super.init()

    loadDataOnRecyclerView()
  }

  override fun initUi() {
    super.initUi()

    showBackButton()
  }

  @SuppressLint("RestrictedApi")
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    getMenuInflater().inflate(R.menu.menu, menu)
    if (menu is MenuBuilder) {
      menu.setOptionalIconsVisible(true)
    }
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.getItemId()) {
      R.id.updateName -> { onNewCategoryName() }
      R.id.delete     -> { deleteCategory() }
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
  }

  fun loadDataOnRecyclerView() {
    viewModel.taskGroup = intent.getStringExtra(PARAM_EXTRA_TASK_GROUP)

    when (intent.getStringExtra(PARAM_EXTRA_TASK_GROUP)) {
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

  fun onNewCategoryName() {
    MaterialDialog(this).show {
      title(R.string.new_name_category)
      input(
        prefill = supportActionBar?.title.toString()
      ) { _, title ->
        val model = CategoryModel(
          name       = title.toString(),
          id         = viewModel.categoryId?.toInt(),
          createdAt  = LocalDateTime.now()
        )
        viewModel.updateCategory(model)

        setToolbarTitle(model.name)
      }
      positiveButton(R.string.button_save)
      negativeButton(R.string.delete)
    }
  }

  fun deleteCategory() {
//    fragment.showConfirmDialog(
//      title               = R.string.delete_title,
//      message             = getString(R.string.delete_task_message, viewModel.itemCategory.value?.name),
//      positiveName        = R.string.delete,
//      positiveCallback    = {
//        val model = viewModel.itemCategory.value ?: return@showConfirmDialog
//
//        viewModel.deleteCategory(model)
//      }
//    )
    MaterialDialog(this).show {
      title(R.string.delete_title)
      message(text = getString(R.string.delete_task_message, viewModel.itemCategory.value?.name))
      positiveButton(R.string.delete) {
        val model = viewModel.itemCategory.value ?: return@positiveButton

        viewModel.deleteCategory(model)
      }
      negativeButton(R.string.cancel)
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
