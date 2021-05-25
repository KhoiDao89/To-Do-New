package vn.htv.fresher.todoapp.presentation.tasklist.taskgroup

import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import vn.htv.fresher.todoapp.R
import vn.htv.fresher.todoapp.presentation.main.TaskGroup
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListFragment

class TaskGroupFragment : TaskListFragment<TaskGroupViewModel>() {
  override var backgroundTintResId: Int = 0

  override val viewModel by sharedViewModel<TaskGroupViewModel>()

  override fun setBackgroundTintButton() {
    when (viewModel.taskGroup) {
      TaskGroup.MY_DAY    -> backgroundTintResId = R.color.bg_my_day_button
      TaskGroup.IMPORTANT -> backgroundTintResId = R.color.bg_important_button
      TaskGroup.DEADLINE  -> backgroundTintResId = R.color.bg_deadline_button
      TaskGroup.ACTION    -> backgroundTintResId = R.color.bg_action_button
    }

    super.setBackgroundTintButton()
  }

  companion object {
    fun newInstance() = TaskGroupFragment()
  }
}