package vn.htv.fresher.todoapp.presentation.main

import android.app.Activity
import android.content.Intent
import android.text.InputType
import android.widget.Toast
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentMainBinding
import vn.htv.fresher.todoapp.domain.model.CategoryModel
import vn.htv.fresher.todoapp.presentation.category.CategoryActivity
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailActivity
import vn.htv.fresher.todoapp.util.ext.showInputDialog

class MainFragment : BaseFragment<FragmentMainBinding>() {

  // MainFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId: Int
    get() = R.layout.fragment_main

  private val viewModel by viewModel<MainViewModel>()

  private val categoryAdapter by lazy {
    CategoryAdapter(
        categoryCallback = {
          CategoryActivity.start(safeActivity, it.toLong())
        },
        taskGroupCallback = {
          // navigate to Category Screen with TaskGroup
        }
    )
  }

  override fun init() {
    super.init()

    binding.viewModel       = viewModel
    binding.eventListeners  = EventListeners()

    viewModel.loadData()
  }

  override fun initUi() {
    super.initUi()
    categoryRecyclerView.apply {
      adapter = categoryAdapter
      addItemDecoration(DefaultItemDecoration(
        resources.getDimensionPixelSize(R.dimen.recyclerview_item_horizontal_margin),
        resources.getDimensionPixelSize(R.dimen.recyclerview_item_vertical_margin) ))
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()
    viewModel.mainItemList.observe(this, Observer {
         categoryAdapter.setItems(it)
       })

    viewModel.addCategoryCompleted.observe(this@MainFragment, Observer {
         CategoryActivity.start(safeActivity, it)
       })
  }

  inner class EventListeners() {
    fun onNewCategory() {
      this@MainFragment.showInputDialog(
        title                 = R.string.new_category,
        hint                  = R.string.new_category_hint,
        positiveName          = R.string.button_create_category,
        positiveTaskCallback  = {
          val model = CategoryModel(
            name      = it,
            createdAt = LocalDateTime.now()
          )

          viewModel.addNewCategory(model)
        }
      )
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.loadData()
  }

  /**
   * Static definition
   */
  companion object {

  /**
   * Create MainFragment instance pattern
   */
    fun newInstance() = MainFragment()
  }
}
