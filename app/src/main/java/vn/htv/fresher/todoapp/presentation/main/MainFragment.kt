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
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailActivity

class MainFragment : BaseFragment<FragmentMainBinding>() {

  // MainFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId: Int
    get() = R.layout.fragment_main

  private val viewModel by viewModel<MainViewModel>()

  private val categoryAdapter by lazy {
    CategoryAdapter(
        categoryCallback = {

        },
        taskGroupCallback = {

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
        resources.getDimensionPixelSize(R.dimen.recyclerview_item_vertical_margin)))
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.mainItemList.observe(this, Observer {
      categoryAdapter.setItems(it)
    })

    viewModel.addCategoryCompleted.observe(this@MainFragment,
      Observer {

      })
  }

  inner class EventListeners {
    fun onNewCategory() {
      MaterialDialog(safeContext).show {
        title(R.string.new_category)
        input(
          hint = resources.getString(R.string.new_category_hint)
        ) { _, title ->
          val model = CategoryModel(
            name      = title.toString(),
            createdAt = LocalDateTime.now()
          )
          viewModel.addNewCategory(model)
        }
        positiveButton(R.string.button_create_category)
        negativeButton(R.string.button_cancel)
      }
    }
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