package vn.htv.fresher.todoapp.presentation.taskdetail

import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentTaskDetailBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.util.ext.timeString

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>() {

  // TaskDetailFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId = R.layout.fragment_task_detail

  private val viewModel by viewModel<TaskDetailViewModel>()

  private var taskName: String? = null

  private val subTaskAdapter by lazy {
    SubTaskAdapter()
  }

  override fun init() {
    super.init()
    binding.subTaskViewModel = viewModel
    binding.deleteEventListeners = DeleteEventListeners()
    viewModel.loadData()
  }

  override fun initUi() {
    super.initUi()

    subtaskRecyclerView.apply {
      adapter = subTaskAdapter
      addItemDecoration(DefaultItemDecoration(
        resources.getDimensionPixelSize(R.dimen.recyclerview_item_horizontal_margin),
        resources.getDimensionPixelSize(R.dimen.small_margin)))
    }
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.taskDetailItem.observe(this, {
      subTaskAdapter.setItems(it)
    })

    viewModel.task.observe(this, {
      taskName = it.name
    })
  }

  inner class DeleteEventListeners {
    fun deleteTask() {
      MaterialDialog(safeContext).show {
        title(R.string.delete_task_title)
        message(text = safeContext.getString(R.string.delete_task_message, taskName))
        positiveButton(R.string.delete_task_ok){
//          viewModel.deleteTask()
          safeActivity.onBackPressed()
          // navigate to Task List Screen
          Toast.makeText(safeContext, "Xoa", Toast.LENGTH_SHORT).show()
        }
        negativeButton(R.string.delete_task_cancel){
          // do nothing
          Toast.makeText(safeContext, "Huy bo", Toast.LENGTH_SHORT).show()
        }
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
    fun newInstance() = TaskDetailFragment()
  }
}