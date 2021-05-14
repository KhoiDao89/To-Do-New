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
import vn.htv.fresher.todoapp.domain.usecase.category.GetCategoryUseCase
import vn.htv.fresher.todoapp.presentation.common.BaseActivity
import vn.htv.fresher.todoapp.util.ext.showConfirmDialog
import vn.htv.fresher.todoapp.util.ext.showInputDialog

class CategoryActivity : BaseActivity() {

  private val viewModel by viewModel<CategoryViewModel>()

  override val fragment  : Fragment
    get() = CategoryFragment.newInstance()

  override val layoutId  : Int
    get() = R.layout.activity_category

  override fun init() {
    super.init()

    val catId = intent.getLongExtra(PARAM_EXTRA_CATEGORY_ID, 0)

    viewModel.categoryId = catId
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
      R.id.delete     -> { deletedCategory() }
    }

    return super.onOptionsItemSelected(item)
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

  fun deletedCategory() {
    fragment.showConfirmDialog(
      title               = R.string.delete_title,
      message             = getString(R.string.delete_task_message, viewModel.itemCategory.value?.name),
      positiveName        = R.string.delete,
      positiveCallback    = {
        val model = viewModel.itemCategory.value ?: return@showConfirmDialog

        viewModel.deleteCategory(model)
      }
    )
  }

  companion object {
    const val PARAM_EXTRA_CATEGORY_ID = "CATEGORY"

    fun start(activity: AppCompatActivity, catId: Long) {
      val intent = Intent(activity, CategoryActivity::class.java)

      intent.apply {
        putExtra(PARAM_EXTRA_CATEGORY_ID, catId)
      }

      activity.startActivity(intent)
    }
  }
}
