package vn.htv.fresher.todoapp.presentation.tasklist.taskgroup

import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.main.TaskGroup
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListFragment
import java.security.InvalidParameterException

class TaskGroupFragment : TaskListFragment<TaskGroupViewModel>() {
  override val backgroundTintResId: Int
    get() = when (viewModel.taskGroup) {
      TaskGroup.MY_DAY    -> R.color.bg_my_day_button
      TaskGroup.IMPORTANT -> R.color.bg_important_button
      TaskGroup.DEADLINE  -> R.color.bg_deadline_button
      TaskGroup.ACTION    -> R.color.bg_action_button
      else                -> throw InvalidParameterException("Unexpected task group [${viewModel.taskGroup}]")
    }

  override val viewModel by sharedViewModel<TaskGroupViewModel>()

  companion object {
    fun newInstance() = TaskGroupFragment()
  }
}