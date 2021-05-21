package vn.htv.fresher.todoapp.presentation.taskdetail

import android.graphics.Paint
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentTaskDetailBinding
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.presentation.note.NoteActivity
import vn.htv.fresher.todoapp.util.ext.*

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>() {

  // TaskDetailFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId = R.layout.fragment_task_detail

  private val viewModel by sharedViewModel <TaskDetailViewModel>()

  private val subTaskAdapter by lazy {
    SubTaskAdapter(
      deleteSubTaskCallback = {
        this.showConfirmDialog(
          title             = R.string.delete_title,
          message           = getString(R.string.delete_task_message, it.name),
          positiveName      = R.string.delete,
          positiveCallback  = { viewModel.deleteSubTask(it) }
        )
      },
      finishedSubTaskCallback     = { viewModel.updateFinishStateSubTask(it) },
      removeDeadlineTaskCallback  = { viewModel.removeDeadlineTask(it) },
      removeReminderTaskCallback  = { viewModel.removeReminderTask(it) },
      removeRepeatTaskCallback    = { viewModel.removeRepeatTask(it) },
      saveNewSubtaskCallback      = {
        this.showInputDialog(
          title             = R.string.next_step,
          hint              = R.string.new_subtask_hint,
          positiveName      = R.string.next_step,
          positiveCallback  = { subTaskName ->
            val taskId = viewModel.task.value?.id ?: return@showInputDialog

            viewModel.saveNewSubTask(taskId, subTaskName)
          }
        )
      },
      updateDeadlineTaskCallback = { task ->
        this.showDateDialog (
          dateCallback = {
            val deadlineTask = it.toLocalDateTime()

            viewModel.deadlineTask(task, deadlineTask)
          }
        )
      },
      updateMyDayTaskCallback     = { viewModel.myDayTask(it) },
      updateNoteTaskCallback      = { NoteActivity.start(safeActivity, it.name, it.note) },
      updateReminderTaskCallback  = { task ->
        this.showDateTimeDialog(
          title             = R.string.select_datetime,
          dateTimeCallback  = {
            val reminderTask = it.toLocalDateTime()

            viewModel.reminderTask(task, reminderTask)
          }
        )
      },
      updateRepeatTaskCallback = { task ->
        this.showListDialog(
          listItems         = R.array.repeat,
          positionCallback  = {
            if (task.deadline != null) viewModel.repeatTask(task, it, task.deadline)
            else viewModel.repeatTask(task, it, LocalDateTime.now())
          }
        )
      }
    )
  }

  override fun init() {
    super.init()

    binding.subTaskViewModel      = viewModel
    binding.deleteEventListeners  = DeleteEventListeners()

    viewModel.loadData()
  }

  override fun initUi() {
    super.initUi()

    subTaskRecyclerView.apply {
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
      binding.taskNameTextView.paintFlags = if (it.finished) Paint.STRIKE_THRU_TEXT_FLAG else 0
    })
  }

  inner class DeleteEventListeners {
    fun deleteTask() {
      this@TaskDetailFragment.showConfirmDialog(
        title             = R.string.delete_title,
        message           = getString(R.string.delete_task_message, viewModel.task.value?.name),
        positiveName      = R.string.delete,
        positiveCallback  = {
          viewModel.deleteTask()
          safeActivity.onBackPressed()
        }
      )
    }
  }

  /**
   * Static definition
   */
  companion object {

    /**
     * Create TaskDetailFragment instance pattern
     */
    fun newInstance() = TaskDetailFragment()
  }
}