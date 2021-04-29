package vn.htv.fresher.todoapp.presentation.taskdetail

import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentMainBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.presentation.main.CategoryAdapter

class TaskDetailFragment : BaseFragment<FragmentMainBinding>() {

  // MainFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId: Int
    get() = R.layout.fragment_task_detail

  private val viewModel by viewModel<TaskDetailViewModel>()

  private val subTaskAdapter by lazy {
    SubTaskAdapter()
  }

  override fun init() {
    binding.viewModel = viewModel
    super.init()
  }

  override fun initUi() {
    super.initUi()
    subtaskRecyclerView.apply {
      adapter = SubTaskAdapter()
      addItemDecoration(DefaultItemDecoration(
        resources.getDimensionPixelSize(R.dimen.recyclerview_item_horizontal_margin),
        resources.getDimensionPixelSize(R.dimen.small_margin)))
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()
    viewModel.taskDetailItem.observe(this, Observer {
      subTaskAdapter.setItems(it)
    })
  }

  /**
   * Static definition
   */
  companion object {

    /**
     * Create MainFragment instance pattern
     */
    fun newInstance() = TaskDetailFragment()
  }
}