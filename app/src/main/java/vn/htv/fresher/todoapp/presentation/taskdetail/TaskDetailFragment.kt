package vn.htv.fresher.todoapp.presentation.taskdetail

import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.list.listItems
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentTaskDetailBinding
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.util.ext.timeString
import vn.htv.fresher.todoapp.util.ext.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.*

internal fun Calendar.formatLocalDateTime(): String {
  return SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).format(this.time)
}

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>() {

  // TaskDetailFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId = R.layout.fragment_task_detail

  private val viewModel by sharedViewModel <TaskDetailViewModel>()

  private var taskName: String? = null

  private val subTaskAdapter by lazy {
    SubTaskAdapter(
      finishedSubTaskCallback = { viewModel.finishedSubTask(it) },
      deleteSubTaskCallback   = { task ->
        MaterialDialog(safeContext).show {
          title(R.string.delete_task_title)
          message(text = safeContext.getString(R.string.delete_task_message, task.name))
          positiveButton(R.string.delete_task_ok){ viewModel.deleteSubTask(task) }
          negativeButton(R.string.delete_task_cancel){}
        }
      },
      updateMyDayTaskCallback = { viewModel.myDayTask(it) },
      updateReminderTaskCallback = { task ->
        MaterialDialog(safeContext).show {
          listItems(R.array.reminder) { _, index, text ->
            showDateTimePicker(task)
            dismiss()
          }
        }
      },
      removeReminderTaskCallback = { viewModel.removeReminderTask(it) },
      updateDeadlineTaskCallback = {
        MaterialDialog(safeContext).show {
          datePicker { _, date ->
            val localDateTime = date.toLocalDateTime()
            viewModel.deadlineTask(it, localDateTime)
          }
        }
      },
      removeDeadlineTaskCallback = { viewModel.removeDeadlineTask(it) },
      updateRepeatTaskCallback = { task ->
        MaterialDialog(safeContext).show {
          listItems(R.array.repeat) { _, index, text ->
            Toast.makeText(safeContext, text, Toast.LENGTH_LONG).show()
            if (task.deadline != null) viewModel.repeatTask(task, 1, task.deadline) else viewModel.repeatTask(task, 1, LocalDateTime.now())
          }
        }
      },
      removeRepeatTaskCallback = { viewModel.removeRepeatTask(it) }
    )
  }

  fun showDateTimePicker(model: TaskModel){
    MaterialDialog(safeContext).show {
      title(text = "Select Date and Time")
      dateTimePicker(requireFutureDateTime = true) { _, dateTime ->
        val localDateTime = dateTime.toLocalDateTime()
        viewModel.reminderTask(model, localDateTime)
        Toast.makeText(safeContext, localDateTime.toString(), Toast.LENGTH_LONG).show()
      }
    }
  }

  override fun init() {
    super.init()
    binding.subTaskViewModel      = viewModel
    binding.deleteEventListeners  = DeleteEventListeners()

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
          viewModel.deleteTask()
          safeActivity.onBackPressed()
        }
        negativeButton(R.string.delete_task_cancel){}
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