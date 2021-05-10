package vn.htv.fresher.todoapp.presentation.taskdetail

import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.list.listItems
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalDateTime
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.databinding.FragmentTaskDetailBinding
import vn.htv.fresher.todoapp.domain.model.SubTaskModel
import vn.htv.fresher.todoapp.domain.model.TaskModel
import vn.htv.fresher.todoapp.presentation.common.BaseFragment
import vn.htv.fresher.todoapp.presentation.common.decoration.DefaultItemDecoration
import vn.htv.fresher.todoapp.util.ext.toLocalDateTime

class TaskDetailFragment : BaseFragment<FragmentTaskDetailBinding>() {

  // TaskDetailFragment class variables
  // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  override val layoutId = R.layout.fragment_task_detail

  private val viewModel by sharedViewModel <TaskDetailViewModel>()

  private var taskId: Int?      = null
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
      updateMyDayTaskCallback     = { viewModel.myDayTask(it) },
      updateReminderTaskCallback  = { task ->
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
      removeDeadlineTaskCallback  = { viewModel.removeDeadlineTask(it) },
      updateRepeatTaskCallback    = { task ->
        MaterialDialog(safeContext).show {
          listItems(R.array.repeat) { _, index, text ->
            Toast.makeText(safeContext, "${index} - ${text}", Toast.LENGTH_LONG).show()
            if (task.deadline != null) viewModel.repeatTask(task, index, task.deadline)
            else viewModel.repeatTask(task, index, LocalDateTime.now())
          }
        }
      },
      removeRepeatTaskCallback  = { viewModel.removeRepeatTask(it) },
      saveNewSubtaskCallback    = {
        MaterialDialog(safeContext).show {
          title(R.string.next_step)
          input(
            hint = resources.getString(R.string.new_subtask_hint)
          ) { _, title ->
            val model = taskId?.let {
              SubTaskModel(
                taskId    = it,
                name      = title.toString(),
                createdAt = LocalDateTime.now()
              )
            }
            model?.let { viewModel.saveNewSubTask(it) }
          }
          positiveButton(R.string.next_step)
          negativeButton(R.string.button_cancel)
        }
      },
      updateNoteTaskCallback = {
        NoteActivity.start(safeActivity, it.name, it.note)
      }
    )
  }

  fun showDateTimePicker(model: TaskModel) {
    MaterialDialog(safeContext).show {
      title(text = getString(R.string.select_datetime))
      dateTimePicker(requireFutureDateTime = true) { _, dateTime ->
        val localDateTime = dateTime.toLocalDateTime()
        viewModel.reminderTask(model, localDateTime)
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
      taskId    = it.id
      taskName  = it.name
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
     * Create TaskDetailFragment instance pattern
     */
    fun newInstance() = TaskDetailFragment()
  }
}