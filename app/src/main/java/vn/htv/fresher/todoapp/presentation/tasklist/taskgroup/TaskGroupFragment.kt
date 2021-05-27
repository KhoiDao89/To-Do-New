package vn.htv.fresher.todoapp.presentation.tasklist.taskgroup

import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import vn.htv.fresher.todoapp.presentation.tasklist.TaskListFragment

class TaskGroupFragment : TaskListFragment<TaskGroupViewModel>() {
  override val backgroundTintResId: Int
    get() = viewModel.taskGroup?.backgroundTintResId ?: 0

  override val viewModel by sharedViewModel<TaskGroupViewModel>()

  companion object {
    fun newInstance() = TaskGroupFragment()
  }
}