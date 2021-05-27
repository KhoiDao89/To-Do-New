package vn.htv.fresher.todoapp.presentation.tasklist

import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.fragment_task_list.*
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentTaskListBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.presentation.taskdetail.TaskDetailActivity
import vn.htv.fresher.todoapp.util.ext.showInputDialog

abstract class TaskListFragment<VM : TaskListViewModel> : BaseFragment<FragmentTaskListBinding>() {
  protected abstract val viewModel: VM

  protected abstract val backgroundTintResId: Int

  override val layoutId: Int
    get() = R.layout.fragment_task_list

  private val taskAdapter by lazy {
    TaskAdapter(
      finishedCallback    = { viewModel.updateFinishStateTask(it) },
      importantCallback   = { viewModel.updateImportantTask(it) },
      taskDetailCallback  = { TaskDetailActivity.start(safeActivity, it) }
    )
  }

  override fun init() {
    super.init()

    binding.viewModel = viewModel
  }

  override fun initUi() {
    super.initUi()

    taskRecycleView.apply {
      adapter = taskAdapter
      addItemDecoration(
        DefaultItemDecoration(
        resources.getDimensionPixelSize(R.dimen.layout_margin_start_end),
        resources.getDimensionPixelSize(R.dimen.layout_margin_top_bottom))
      )
    }

    setBackgroundTintButton()
  }

  override fun registerListeners() {
    super.registerListeners()

    taskFloatingActionButton.setOnClickListener {
      showInputDialog(
        title         = R.string.new_task,
        hint          = R.string.new_task_hint,
        positiveName  = R.string.button_create_task,
        saveCallback  = { viewModel.addTask(it) }
      )
    }
  }

  override fun onResume() {
    super.onResume()

    viewModel.loadData()
  }

  override fun registerLiveDataListener() {
    super.registerLiveDataListener()

    viewModel.addTaskCompleted.observe(this,{
      if (!it) return@observe
      viewModel.loadData()
    })

    viewModel.itemList.observe(this, {
      taskAdapter.setItems(it)
    })

    viewModel.updateTaskCompleted.observe(this, {
      if (!it) return@observe
      viewModel.loadData()
    })
  }

  private fun setBackgroundTintButton() {
    taskFloatingActionButton.backgroundTintList =
      ContextCompat.getColorStateList(safeContext, backgroundTintResId)
  }
}